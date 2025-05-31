package com.structura.steel.commons.dto.core.request.delivery;

import java.math.BigDecimal;

public record DeliveryOrderRequestDto(
        Long purchaseOrderId,
        Long saleOrderId,
        Long vehicleId,
        String driverName,
        String deliveryAddress,
        String confirmationFromPartner,
        String confirmationFromFactory,
        BigDecimal distance,
        BigDecimal deliveryUnitPrice,
        BigDecimal additionalFees,
        BigDecimal totalDeliveryFee,
        String deliveryType,
        String deliveryOrderNote
) {}
