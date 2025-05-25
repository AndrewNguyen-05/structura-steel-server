package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;
import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.enumeration.DebtAccountType;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.coreservice.entity.*;
import com.structura.steel.coreservice.mapper.DebtPaymentMapper;
import com.structura.steel.coreservice.repository.*;
import com.structura.steel.coreservice.service.DebtPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DebtPaymentServiceImpl implements DebtPaymentService {

    private final PurchaseDebtRepository purchaseDebtRepository;
    private final SaleDebtRepository saleDebtRepository;
    private final DeliveryDebtRepository deliveryDebtRepository;
    private final DebtPaymentRepository debtPaymentRepository;
    private final DebtPaymentMapper debtPaymentMapper;
    private final PartnerFeignClient partnerFeignClient;
    // Giả sử bạn có VehicleFeignClient hoặc cách khác để lấy Partner từ Vehicle
    // private final VehicleFeignClient vehicleFeignClient;

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
                partnerId = pDebt.getPurchaseOrder().getSupplierId();
                accountType = DebtAccountType.PAYABLE;
                purchaseDebtRepository.save(pDebt);
                break;
            case SALE_DEBT:
                SaleDebt sDebt = saleDebtRepository.findById(debtId)
                        .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", debtId));
                validateAndProcessPayment(sDebt, amountPaid);
                partnerId = sDebt.getSaleOrder().getPartnerId();
                accountType = DebtAccountType.RECEIVABLE;
                saleDebtRepository.save(sDebt);
                break;
            case DELIVERY_DEBT:
                DeliveryDebt dDebt = deliveryDebtRepository.findById(debtId)
                        .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", debtId));
                validateAndProcessPayment(dDebt, amountPaid);
                partnerId = getTransporterId(dDebt.getDeliveryOrder()); // **QUAN TRỌNG: Cần logic thực tế**
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
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }

    private void updateStatus(SaleDebt debt) {
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.PAID);
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }

    private void updateStatus(DeliveryDebt debt) {
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.PAID);
        } else {
            debt.setStatus(DebtStatus.PARTIALLY_PAID);
        }
    }


    /**
     * **QUAN TRỌNG:** Phương thức này cần được triển khai DỰA TRÊN THIẾT KẾ CỦA BẠN.
     * Làm thế nào để từ DeliveryOrder lấy được PartnerId của nhà vận chuyển?
     *
     * Gợi ý:
     * 1. **Thêm `transporterId` vào `DeliveryOrder`:** Đây là cách sạch sẽ nhất. Khi tạo DeliveryOrder, bạn phải biết ai là nhà vận chuyển và lưu ID của họ.
     * 2. **Sử dụng `VehicleId`:** Nếu mỗi `Vehicle` chỉ thuộc về một `Partner` (Transporter), bạn cần:
     * - Thêm `VehicleFeignClient` vào Core Service.
     * - Gọi `VehicleFeignClient` để lấy thông tin `Vehicle` bằng `deliveryOrder.getVehicleId()`.
     * - Lấy `partnerId` từ thông tin `Vehicle` trả về.
     * 3. **Dựa vào `PurchaseOrder` hoặc `SaleOrder`:** Nếu việc vận chuyển luôn gắn liền với một đơn mua/bán và nhà vận chuyển được xác định ở đó.
     *
     * Vì tôi không có đủ thông tin, tôi sẽ tạm **ném ra lỗi**. Bạn BẮT BUỘC phải thay thế nó.
     */
    private Long getTransporterId(DeliveryOrder deliveryOrder) {
        Long vehicleId = deliveryOrder.getVehicleId();
        if (vehicleId == null) {
            throw new ResourceNotFoundException("VehicleId", "deliveryOrderId", deliveryOrder.getId());
        }

        log.error("CRITICAL: 'getTransporterId' method needs a real implementation!");
        log.error("Cannot determine Transporter Partner from DeliveryOrder ID {} (Vehicle ID: {}).",
                deliveryOrder.getId(), vehicleId);
        log.error("Please implement logic, e.g., by adding transporterId to DeliveryOrder or using a VehicleFeignClient.");

        // **!!! THAY THẾ DÒNG DƯỚI BẰNG LOGIC CỦA BẠN !!!**
        throw new UnsupportedOperationException(
                "Cannot determine Transporter ID. Please implement 'getTransporterId' " +
                        "in DebtPaymentServiceImpl based on your system design (e.g., add transporterId " +
                        "to DeliveryOrder or implement VehicleFeignClient)."
        );

        // Ví dụ (nếu bạn có VehicleFeignClient):
        // try {
        //     VehicleResponseDto vehicle = vehicleFeignClient.getVehicleById(vehicleId);
        //     if (vehicle == null || vehicle.getPartnerId() == null) {
        //         throw new ResourceNotFoundException("Partner", "vehicleId", vehicleId);
        //     }
        //     return vehicle.getPartnerId();
        // } catch (Exception e) {
        //      log.error("Failed to get partner via VehicleFeignClient for vehicle {}", vehicleId, e);
        //      throw new RuntimeException("Failed to get transporter info.", e);
        // }
    }
}