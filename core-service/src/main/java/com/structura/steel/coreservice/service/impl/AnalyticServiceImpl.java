package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.dto.core.response.analytic.AgingReportDto;
import com.structura.steel.commons.dto.core.response.analytic.DebtStatusDistributionDto;
import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.SummaryDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.DebtType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.analytic.AgingProjection;
import com.structura.steel.coreservice.entity.analytic.DebtStatusDistributionProjection;
import com.structura.steel.coreservice.entity.analytic.RevenueOverTimeProjection;
import com.structura.steel.coreservice.repository.DeliveryDebtRepository;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
import com.structura.steel.coreservice.repository.PurchaseDebtRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.repository.SaleDebtRepository;
import com.structura.steel.coreservice.repository.SaleOrderDetailRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnalyticServiceImpl implements AnalyticService {

    private final SaleOrderRepository saleOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;

    private final SaleOrderDetailRepository saleOrderDetailRepository;

    private final SaleDebtRepository saleDebtRepository;
    private final PurchaseDebtRepository purchaseDebtRepository;
    private final DeliveryDebtRepository deliveryDebtRepository;

    @Transactional(readOnly = true)
    public List<RevenueDataPointDto> getRevenueOverTime(Instant start, Instant end) {

        List<RevenueOverTimeProjection> projections = saleOrderRepository.findRevenueOverTime(start, end);

        return projections.stream()
                .map(p -> new RevenueDataPointDto(p.getDateLabel(), p.getTotalRevenue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopItemDto> getTopCustomers(Instant start, Instant end, int limit) {

        return saleOrderRepository.findTopCustomers(start, end, limit).stream()
                .map(p -> new TopItemDto(p.getName(), p.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopItemDto> getTopProducts(Instant start, Instant end, int limit) {

        return saleOrderDetailRepository.findTopProducts(start, end, limit).stream()
                .map(p -> new TopItemDto(p.getName(), p.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SummaryDto getSummary(Instant start, Instant end) {

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCostOfGoods = BigDecimal.ZERO;
        BigDecimal totalDeliveryCost = BigDecimal.ZERO;

        // 1. Lấy danh sách các đơn sale đã DONE trong kỳ cùng với các chi phí liên quan
        List<SaleOrder> completedSaleOrders = saleOrderRepository.findAllByStatusAndUpdatedAtBetweenWithJoins(OrderStatus.DONE, start, end);

        // 2. Lặp qua từng đơn sale để tính
        for (SaleOrder saleOrder : completedSaleOrders) {
            // Cộng dồn doanh thu từ sale
            totalRevenue = totalRevenue.add(saleOrder.getTotalAmount());

            // Lấy đơn purchase tương ứng
            PurchaseOrder purchaseOrder = saleOrder.getPurchaseOrder();
            if (purchaseOrder != null) {
                // Cộng dồn giá vốn từ purchase
                totalCostOfGoods = totalCostOfGoods.add(purchaseOrder.getTotalAmount());

                // Cộng dồn chi phí vận chuyển từ các delivery order của purchase đó
                for (DeliveryOrder deliveryOrder : purchaseOrder.getDeliveryOrders()) {
                    if (deliveryOrder.getStatus() == OrderStatus.DONE) {
                        totalDeliveryCost = totalDeliveryCost.add(deliveryOrder.getTotalDeliveryFee());
                    }
                }
            }
        }

        BigDecimal totalExpenses = totalCostOfGoods.add(totalDeliveryCost);
        BigDecimal grossProfit = totalRevenue.subtract(totalExpenses);

        List<DebtStatus> unpaidStatuses = List.of(DebtStatus.UNPAID, DebtStatus.PARTIALLY_PAID);
        BigDecimal totalDebtReceivable = saleDebtRepository.sumRemainingAmountByStatusIn(unpaidStatuses);

        BigDecimal purchasePayable = purchaseDebtRepository.sumRemainingAmountByStatusIn(unpaidStatuses);
        BigDecimal deliveryPayable = deliveryDebtRepository.sumRemainingAmountByStatusIn(unpaidStatuses);
        BigDecimal totalDebtPayable = purchasePayable.add(deliveryPayable);

        return new SummaryDto(totalRevenue, totalCostOfGoods, totalDeliveryCost, grossProfit, totalDebtReceivable, totalDebtPayable);
    }

    @Transactional(readOnly = true)
    public List<DebtStatusDistributionDto> getDebtStatusDistribution(DebtType type) {
        List<DebtStatus> unpaidStatuses = List.of(DebtStatus.UNPAID, DebtStatus.PARTIALLY_PAID);
        Map<DebtStatus, DebtStatusDistributionDto> aggregationMap = new HashMap<>();

        if (DebtType.SALE_DEBT.equals(type)) {

            List<DebtStatusDistributionProjection> projections = saleDebtRepository.getDebtStatusDistribution(unpaidStatuses);
            projections.forEach(p -> aggregateStatus(aggregationMap, p));

        } else if (DebtType.PURCHASE_DEBT.equals(type)) {

            List<DebtStatusDistributionProjection> purchaseProjections = purchaseDebtRepository.getDebtStatusDistribution(unpaidStatuses);
            purchaseProjections.forEach(p -> aggregateStatus(aggregationMap, p));

        } else if (DebtType.DELIVERY_DEBT.equals(type)) {

            List<DebtStatusDistributionProjection> deliveryProjections = deliveryDebtRepository.getDebtStatusDistribution(unpaidStatuses);
            deliveryProjections.forEach(p -> aggregateStatus(aggregationMap, p));
        }

        return new ArrayList<>(aggregationMap.values());
    }

    @Transactional(readOnly = true)
    public List<AgingReportDto> getAgingReport(DebtType type) {
        List<DebtStatus> unpaidStatuses = List.of(DebtStatus.UNPAID, DebtStatus.PARTIALLY_PAID);

        if (DebtType.SALE_DEBT.equals(type)) {
            List<AgingProjection> receivableData = saleDebtRepository.getReceivableAgingData(unpaidStatuses);
            return processAgingData(receivableData);
        }

        // Logic cho PAYABLE (bao gồm cả PURCHASE và DELIVERY)
        List<AgingProjection> payableData = new ArrayList<>();
        payableData.addAll(purchaseDebtRepository.getPayableAgingData(unpaidStatuses));
        payableData.addAll(deliveryDebtRepository.getPayableAgingData(unpaidStatuses));

        return processAgingData(payableData);
    }

    /**
     * Helper method để xử lý logic tính toán tuổi nợ từ dữ liệu projection.
     * Dùng chung cho cả nợ phải thu và phải trả.
     */
    private List<AgingReportDto> processAgingData(List<AgingProjection> data) {
        // Nhóm các khoản nợ theo tên đối tác
        Map<String, List<AgingProjection>> groupedByPartner =
                data.stream().collect(Collectors.groupingBy(AgingProjection::getPartnerName));

        List<AgingReportDto> result = new ArrayList<>();

        // Tính toán các bucket tuổi nợ cho mỗi đối tác
        groupedByPartner.forEach((partnerName, debts) -> {
            BigDecimal current = BigDecimal.ZERO;
            BigDecimal dueIn31_60 = BigDecimal.ZERO;
            BigDecimal dueIn61_90 = BigDecimal.ZERO;
            BigDecimal over90 = BigDecimal.ZERO;

            for (AgingProjection debt : debts) {
                long days = java.time.Duration.between(debt.getCreatedAt(), Instant.now()).toDays();
                if (days <= 30) {
                    current = current.add(debt.getRemainingAmount());
                } else if (days <= 60) {
                    dueIn31_60 = dueIn31_60.add(debt.getRemainingAmount());
                } else if (days <= 90) {
                    dueIn61_90 = dueIn61_90.add(debt.getRemainingAmount());
                } else {
                    over90 = over90.add(debt.getRemainingAmount());
                }
            }
            BigDecimal totalDue = current.add(dueIn31_60).add(dueIn61_90).add(over90);

            // Chỉ thêm vào kết quả nếu có nợ
            if (totalDue.compareTo(BigDecimal.ZERO) > 0) {
                result.add(new AgingReportDto(partnerName, totalDue, current, dueIn31_60, dueIn61_90, over90));
            }
        });

        return result;
    }


    private void aggregateStatus(Map<DebtStatus, DebtStatusDistributionDto> map, DebtStatusDistributionProjection p) {
        map.merge(p.getStatus(),
                new DebtStatusDistributionDto(p.getStatus().text(), p.getCount(), p.getTotalAmount()),
                (oldVal, newVal) -> new DebtStatusDistributionDto(
                        oldVal.status(),
                        oldVal.count() + newVal.count(),
                        oldVal.totalAmount().add(newVal.totalAmount())
                )
        );
    }
}
