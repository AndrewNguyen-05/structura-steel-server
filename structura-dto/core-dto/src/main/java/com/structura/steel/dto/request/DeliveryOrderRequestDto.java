package com.structura.steel.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryOrderRequestDto(
        Long purchaseOrderId,
        Long saleOrderId,
        Instant deliveryDate,
        Long vehicleId,
        String driverName,
        Long warehouseId,
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
