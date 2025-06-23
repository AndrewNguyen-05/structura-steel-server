package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;
import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.enumeration.DebtAccountType;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.DebtType;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.coreservice.entity.*;
import com.structura.steel.coreservice.mapper.DebtPaymentMapper;
import com.structura.steel.coreservice.repository.*;
import com.structura.steel.coreservice.service.DebtPaymentService;
import com.structura.steel.coreservice.service.DeliveryOrderService;
import com.structura.steel.coreservice.service.PurchaseOrderService;
import com.structura.steel.coreservice.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DebtPaymentServiceImpl implements DebtPaymentService {

    private final PurchaseDebtRepository purchaseDebtRepository;
    private final SaleDebtRepository saleDebtRepository;
    private final DeliveryDebtRepository deliveryDebtRepository;
    private final DebtPaymentRepository debtPaymentRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;

    private final PurchaseOrderService purchaseOrderService;
    private final SaleOrderService saleOrderService;
    private final DeliveryOrderService deliveryOrderService;

    private final DebtPaymentMapper debtPaymentMapper;

    private final PartnerFeignClient partnerFeignClient;

    @Override
    public DebtPaymentResponseDto recordPayment(DebtPaymentRequestDto dto) {
        BigDecimal amountPaid = dto.amountPaid();
        Long partnerId;
        DebtAccountType accountType;
        Long debtId = dto.debtId();

        switch (dto.debtType()) {
            case PURCHASE_DEBT:
                PurchaseDebt pDebt = purchaseDebtRepository.findById(debtId)
                        .orElseThrow(() -> new ResourceNotFoundException("PurchaseDebt", "id", debtId));
                validateAndProcessPayment(pDebt, amountPaid);
                partnerId = pDebt.getPurchaseOrder().getSupplier().id();
                accountType = DebtAccountType.PAYABLE;
                purchaseDebtRepository.save(pDebt);
                break;
            case SALE_DEBT:
                SaleDebt sDebt = saleDebtRepository.findById(debtId)
                        .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", debtId));
                validateAndProcessPayment(sDebt, amountPaid);
                partnerId = sDebt.getSaleOrder().getPartner().id();
                accountType = DebtAccountType.RECEIVABLE;
                saleDebtRepository.save(sDebt);
                break;
            case DELIVERY_DEBT:
                DeliveryDebt dDebt = deliveryDebtRepository.findById(debtId)
                        .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", debtId));
                validateAndProcessPayment(dDebt, amountPaid);
                partnerId = dDebt.getDeliveryOrder().getPartner().id();
                accountType = DebtAccountType.PAYABLE;
                deliveryDebtRepository.save(dDebt);
                break;
            default:
                throw new BadRequestException("Invalid Debt Type: " + dto.debtType());
        }

        // 2. Save DebtPayment
        DebtPayment payment = debtPaymentMapper.toDebtPayment(dto);
        payment.setPartnerId(partnerId);
        DebtPayment savedPayment = debtPaymentRepository.save(payment);

        // 3. Update Partner's Debt via Feign Client (Subtract the payment)
        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(amountPaid.negate(), accountType));
            log.info("Decreased {} debt for partner {} by {}", accountType, partnerId, amountPaid);
        } catch (Exception e) {
            log.error("Failed to update partner {} debt after payment for {} (ID: {}): {}",
                    partnerId, dto.debtType(), debtId, e.getMessage(), e);
            // Xử lý lỗi: Rollback transaction và báo lỗi cho người dùng
            throw new RuntimeException("Failed to update partner debt. Payment processing failed.", e);
        }

        return debtPaymentMapper.toDebtPaymentResponseDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DebtPaymentResponseDto> getPaymentHistoryForOrder(Long orderId, DebtType debtType) {
        // 1. Dựa vào debtType và orderId, tìm ra các bản ghi công nợ (Debt) liên quan.
        // Một order có thể có nhiều khoản nợ (ví dụ: mỗi sản phẩm một khoản nợ)
        List<Long> debtIds;

        switch (debtType) {
            case PURCHASE_DEBT:
                List<PurchaseDebt> purchaseDebts = purchaseDebtRepository.findByPurchaseOrderId(orderId);
                if (purchaseDebts.isEmpty()) {
                    return Collections.emptyList();
                }
                debtIds = purchaseDebts.stream().map(PurchaseDebt::getId).collect(Collectors.toList());
                break;
            case SALE_DEBT:
                List<SaleDebt> saleDebts = saleDebtRepository.findBySaleOrderId(orderId);
                if (saleDebts.isEmpty()) {
                    return Collections.emptyList();
                }
                debtIds = saleDebts.stream().map(SaleDebt::getId).collect(Collectors.toList());
                break;
            case DELIVERY_DEBT:
                List<DeliveryDebt> deliveryDebts = deliveryDebtRepository.findByDeliveryOrderId(orderId);
                if (deliveryDebts.isEmpty()) {
                    return Collections.emptyList();
                }
                debtIds = deliveryDebts.stream().map(DeliveryDebt::getId).collect(Collectors.toList());
                break;
            default:
                throw new BadRequestException("Invalid Debt Type: " + debtType);
        }

        // 2. Từ danh sách debtIds, tìm tất cả các lần thanh toán (DebtPayment) tương ứng
        List<DebtPayment> payments = debtPaymentRepository.findByDebtTypeAndDebtIdIn(debtType, debtIds);

        // 3. Map danh sách DebtPayment sang DebtPaymentResponseDto
        return payments.stream()
                .map(debtPaymentMapper::toDebtPaymentResponseDto)
                .collect(Collectors.toList());
    }

    private void validateAndProcessPayment(PurchaseDebt debt, BigDecimal amountPaid) {
        validatePayment(debt.getRemainingAmount(), amountPaid, debt.getStatus());
        debt.setRemainingAmount(debt.getRemainingAmount().subtract(amountPaid));
        updateStatus(debt);
    }

    private void validateAndProcessPayment(SaleDebt debt, BigDecimal amountPaid) {
        validatePayment(debt.getRemainingAmount(), amountPaid, debt.getStatus());
        debt.setRemainingAmount(debt.getRemainingAmount().subtract(amountPaid));
        updateStatus(debt);
    }

    private void validateAndProcessPayment(DeliveryDebt debt, BigDecimal amountPaid) {
        validatePayment(debt.getRemainingAmount(), amountPaid, debt.getStatus());
        debt.setRemainingAmount(debt.getRemainingAmount().subtract(amountPaid));
        updateStatus(debt);
    }

    // --- Helper Methods ---

    private void validatePayment(BigDecimal remainingAmount, BigDecimal amountPaid, DebtStatus currentStatus) {
        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Payment amount must be positive.");
        }
        if (amountPaid.compareTo(remainingAmount) > 0) {
            throw new BadRequestException("Payment amount (" + amountPaid + ") exceeds remaining debt (" + remainingAmount + ").");
        }
        if (currentStatus == DebtStatus.PAID || currentStatus == DebtStatus.CANCELLED) {
            throw new BadRequestException("Debt is already " + currentStatus + ".");
        }
    }

    // Overloading updateStatus for different debt types
    private void updateStatus(PurchaseDebt debt) {
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.PAID);
            PurchaseOrder po = purchaseOrderRepository.findById(debt.getPurchaseOrder().getId()).orElse(null);
            purchaseOrderService.checkAndUpdateDoneStatus(po);
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }

    private void updateStatus(SaleDebt debt) {
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.PAID);
            SaleOrder so = saleOrderRepository.findById(debt.getSaleOrder().getId()).orElse(null);
            saleOrderService.checkAndUpdateDoneStatus(so);
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }

    private void updateStatus(DeliveryDebt debt) {
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.PAID);
            DeliveryOrder order = deliveryOrderRepository.findById(debt.getDeliveryOrder().getId()).orElse(null);
            deliveryOrderService.checkAndUpdateDoneStatus(order);
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }
}