package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.dto.core.response.report.DailySummaryDto;
import com.structura.steel.commons.dto.core.response.report.PayableReportDto;
import com.structura.steel.commons.dto.core.response.report.ProfitLossReportDto;
import com.structura.steel.commons.dto.core.response.report.ReceivableReportDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.DebtType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.enumeration.ReportOrderType;
import com.structura.steel.commons.enumeration.TransactionType;
import com.structura.steel.coreservice.utils.ExcelReportGenerator;
import com.structura.steel.coreservice.entity.*;
import com.structura.steel.coreservice.repository.*;
import com.structura.steel.coreservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PartnerFeignClient partnerFeignClient;

    private final ExcelReportGenerator excelGenerator;

    private final SaleDebtRepository saleDebtRepository;
    private final PurchaseDebtRepository purchaseDebtRepository;
    private final DeliveryDebtRepository deliveryDebtRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DebtPaymentRepository debtPaymentRepository;

    @Override
    public List<ReceivableReportDto> getReceivableReportData(Instant start, Instant end) {

        // Định nghĩa các trạng thái "chưa trả xong"
        List<DebtStatus> unpaidStatuses = List.of(DebtStatus.UNPAID, DebtStatus.PARTIALLY_PAID);

        List<SaleDebt> debts = saleDebtRepository.findByStatusInAndCreatedAtBetween(unpaidStatuses, start, end);

        return debts.stream()
                .map(debt -> new ReceivableReportDto(
                        debt.getSaleOrder().getPartner().partnerName(),
                        debt.getSaleOrder().getExportCode(),
                        debt.getCreatedAt(),
                        debt.getRemainingAmount(),
                        debt.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<PayableReportDto> getPayableReportData(Instant start, Instant end) {

        List<DebtStatus> unpaidStatuses = List.of(DebtStatus.UNPAID, DebtStatus.PARTIALLY_PAID);

        // Lấy danh sách nợ mua hàng từ repository
        List<PurchaseDebt> purchaseDebts = purchaseDebtRepository.findByStatusInAndCreatedAtBetween(unpaidStatuses, start, end);
        Stream<PayableReportDto> purchaseStream = purchaseDebts.stream()
                .map(debt -> new PayableReportDto(
                        debt.getPurchaseOrder().getSupplier().partnerName(),
                        DebtType.PURCHASE_DEBT,
                        debt.getPurchaseOrder().getImportCode(),
                        debt.getCreatedAt(),
                        debt.getRemainingAmount()
                ));

        // Lấy danh sách nợ vận chuyển từ repository
        List<DeliveryDebt> deliveryDebts = deliveryDebtRepository.findByStatusInAndCreatedAtBetween(unpaidStatuses, start, end);
        Stream<PayableReportDto> deliveryStream = deliveryDebts.stream()
                .map(debt -> new PayableReportDto(
                        debt.getDeliveryOrder().getPartner().partnerName(),
                        DebtType.DELIVERY_DEBT,
                        debt.getDeliveryOrder().getDeliveryCode(),
                        debt.getCreatedAt(),
                        debt.getRemainingAmount()
                ));

        // Gộp hai danh sách lại và trả về
        return Stream.concat(purchaseStream, deliveryStream).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfitLossReportDto> getProfitLossReportData(Instant start, Instant end) {

        // Các trạng thái được xem là đã hoàn thành để tính lợi nhuận
        List<OrderStatus> targetStatuses = List.of(OrderStatus.DONE);

        // Lấy các đơn SaleOrder đã hoàn thành trong thời gian
        List<SaleOrder> completedSaleOrders = saleOrderRepository.findByStatusInAndUpdatedAtBetween(targetStatuses, start, end);

        // Nếu không có đơn hàng nào, trả về danh sách rỗng ngay lập tức
        if (completedSaleOrders.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProfitLossReportDto> results = new ArrayList<>();
        for (SaleOrder so : completedSaleOrders) {
            // A. Doanh thu
            BigDecimal revenue = so.getTotalAmount();

            // B. Giá vốn: Chỉ lấy PO liên quan đến SO này và trong khoảng thời gian phù hợp
            BigDecimal costOfGoods = calculateCostOfGoods(so);

            // C. Chi phí vận chuyển: Tính riêng cho từng loại
            BigDecimal deliveryCost = calculateDeliveryCost(so);

            // D. Lợi nhuận gộp
            BigDecimal grossProfit = revenue.subtract(costOfGoods.add(deliveryCost));

            // E. Tạo DTO kết quả
            results.add(new ProfitLossReportDto(
                    so.getExportCode(),
                    so.getPartner().partnerName(),
                    so.getUpdatedAt(),
                    revenue,
                    costOfGoods,
                    deliveryCost,
                    grossProfit
            ));
        }
        return results;
    }

    @Override
    public DailySummaryDto getDailyActivityReportData() {

        LocalDate today = LocalDate.now();

        Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

//        ZoneId zone = ZoneOffset.UTC;
//        Instant startOfDay = today.atStartOfDay(zone).toInstant();
//        Instant endOfDay = today.plusDays(1).atStartOfDay(zone).toInstant();

        // === PHẦN 1: TÓM TẮT NHANH ===
        long newSaleOrdersCount = saleOrderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        BigDecimal newSaleOrdersValue = saleOrderRepository.sumTotalAmountByCreatedAtBetween(startOfDay, endOfDay)
                .orElse(BigDecimal.ZERO);
        long newPurchaseOrdersCount = purchaseOrderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long completedDeliveriesCount = deliveryOrderRepository.countByStatusInAndUpdatedAtBetween(List.of(OrderStatus.DELIVERED, OrderStatus.DONE), startOfDay, endOfDay);
        BigDecimal totalDebtAmountReceived = debtPaymentRepository.sumAmountPaidByDateAndType(startOfDay, endOfDay, DebtType.SALE_DEBT)
                .orElse(BigDecimal.ZERO);
        BigDecimal totalDebtAmountPaid = debtPaymentRepository.sumAmountPaidByDateAndTypes(startOfDay, endOfDay, List.of(DebtType.PURCHASE_DEBT, DebtType.DELIVERY_DEBT))
                .orElse(BigDecimal.ZERO);

        DailySummaryDto.SummarySection summary = new DailySummaryDto.SummarySection(
                newSaleOrdersCount, newSaleOrdersValue, newPurchaseOrdersCount,
                completedDeliveriesCount, totalDebtAmountReceived, totalDebtAmountPaid
        );

        // === PHẦN 2: CHI TIẾT CÁC ĐƠN HÀNG MỚI TẠO ===
        Stream<DailySummaryDto.NewOrder> newSales = saleOrderRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(so -> new DailySummaryDto.NewOrder(so.getCreatedAt(), ReportOrderType.SALE, so.getExportCode(), so.getPartner().partnerName(), so.getTotalAmount(), so.getCreatedBy()));

        Stream<DailySummaryDto.NewOrder> newPurchases = purchaseOrderRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(po -> new DailySummaryDto.NewOrder(po.getCreatedAt(), ReportOrderType.PURCHASE, po.getImportCode(), po.getSupplier().partnerName(), po.getTotalAmount(), po.getCreatedBy()));

        Stream<DailySummaryDto.NewOrder> newDeliveries = deliveryOrderRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
                .map(de -> new DailySummaryDto.NewOrder(de.getCreatedAt(), ReportOrderType.DELIVERY, de.getDeliveryCode(), de.getPartner().partnerName(), de.getTotalDeliveryFee(), de.getCreatedBy()));

        List<DailySummaryDto.NewOrder> newOrders = Stream.concat(Stream.concat(newSales, newPurchases), newDeliveries)
                .sorted(Comparator.comparing(DailySummaryDto.NewOrder::createdAt))
                .collect(Collectors.toList());

        // === PHẦN 3: CHI TIẾT GIAO HÀNG HOÀN TẤT ===
        List<DailySummaryDto.CompletedDelivery> completedDeliveries = deliveryOrderRepository
                .findByStatusInAndUpdatedAtBetween(List.of(OrderStatus.DELIVERED, OrderStatus.DONE), startOfDay, endOfDay).stream()
                .map(de -> {
                    String originalOrderCode = de.getPurchaseOrder() != null ? de.getPurchaseOrder().getImportCode() : "N/A";
                    return new DailySummaryDto.CompletedDelivery(
                            de.getDeliveryCode(),
                            originalOrderCode,
                            de.getPartner().partnerName(),
                            de.getDeliveryAddress(),
                            de.getUpdatedAt());
                })
                .collect(Collectors.toList());

        // === PHẦN 4: CHI TIẾT GIAO DỊCH THANH TOÁN ===

        // BƯỚC A: Lấy tất cả các giao dịch thanh toán trong ngày từ database của core-service
        List<DebtPayment> dailyPayments = debtPaymentRepository.findByPaymentDateBetween(startOfDay, endOfDay);

        // BƯỚC B: Gom ID và gọi Feign Client một lần duy nhất để lấy thông tin Partners
        Set<Long> partnerIds = dailyPayments.stream()
                .map(DebtPayment::getPartnerId)
                .collect(Collectors.toSet());

        // Tạo một Map rỗng để tra cứu, key là ID, value là Tên Partner
        Map<Long, String> partnerNameMap = new HashMap<>();

        if (!partnerIds.isEmpty()) {
            // Chỉ gọi API khi có ID cần tra cứu
            List<PartnerResponseDto> partnersFromApi = partnerFeignClient.getPartnersByIds(new ArrayList<>(partnerIds));
            // Đưa kết quả vào Map để tra cứu hiệu quả
            partnerNameMap = partnersFromApi.stream()
                    .collect(Collectors.toMap(PartnerResponseDto::id, PartnerResponseDto::partnerName));
        }

        // BƯỚC C: Map danh sách thanh toán sang DTO, sử dụng Map đã tạo để lấy tên partner
        Map<Long, String> finalPartnerNameMap = partnerNameMap; // final variable for lambda
        List<DailySummaryDto.PaymentTransaction> paymentTransactions = dailyPayments.stream()
                .map(p -> {
                    // Tra cứu tên partner trong Map, nếu không có thì dùng tên mặc định
                    String partnerName = finalPartnerNameMap.getOrDefault(p.getPartnerId(), "Partner ID: " + p.getPartnerId());

                    TransactionType transactionType = (p.getDebtType() == DebtType.SALE_DEBT) ? TransactionType.INCOME : TransactionType.EXPENSE;

                    return new DailySummaryDto.PaymentTransaction(
                            p.getPaymentDate(),
                            transactionType,
                            partnerName,
                            p.getAmountPaid(),
                            p.getPaymentMethod(),
                            p.getNotes());
                })
                .collect(Collectors.toList());

        // === TỔNG HỢP VÀ TRẢ VỀ ===
        return new DailySummaryDto(summary, newOrders, completedDeliveries, paymentTransactions);
    }

    /**
     * Tính giá vốn cho một SaleOrder cụ thể
     */
    private BigDecimal calculateCostOfGoods(SaleOrder saleOrder) {

        if (saleOrder.getProject() == null || saleOrder.getProject().id() == null) {
            return BigDecimal.ZERO;
        }

        // Chỉ lấy PO của cùng project và được tạo TRƯỚC HOẶC CÙNG THỜI GIAN với SO
        List<PurchaseOrder> relatedPOs = purchaseOrderRepository
                .findByProjectIdAndCreatedAtAfterAndStatus(
                        saleOrder.getProject().id(),
                        saleOrder.getCreatedAt().plusSeconds(1), // Thêm 1 giây để bao gồm cùng thời điểm
                        OrderStatus.DONE.name()
                );

        return relatedPOs.stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tính chi phí vận chuyển cho một SaleOrder cụ thể
     */
    private BigDecimal calculateDeliveryCost(SaleOrder saleOrder) {

        BigDecimal totalDeliveryCost = BigDecimal.ZERO;

        // Nếu không có project thì bỏ qua
        if (saleOrder.getProject() != null && saleOrder.getProject().id() != null) {
            // Lấy các PO cùng project và đã hoàn thành (DONE), được tạo trước hoặc bằng thời gian của SO
            List<PurchaseOrder> relatedPOs = purchaseOrderRepository
                    .findByProjectIdAndCreatedAtAfterAndStatus(
                            saleOrder.getProject().id(),
                            saleOrder.getCreatedAt().plusSeconds(1),
                            OrderStatus.DONE.name()
                    );

            // Lấy tổng chi phí vận chuyển từ các delivery order của PO
            totalDeliveryCost = relatedPOs.stream()
                    .flatMap(po -> po.getDeliveryOrders().stream())
                    .filter(deliveryOrder -> deliveryOrder.getStatus() == OrderStatus.DONE)
                    .map(DeliveryOrder::getTotalDeliveryFee)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return totalDeliveryCost;
    }
}