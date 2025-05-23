package com.structura.steel.commons.dto.core.request;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryOrderRequestDto(
        Long purchaseOrderId,
        Long saleOrderId,
        Instant deliveryDate,
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
