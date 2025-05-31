package com.structura.steel.commons.dto.core.response.delivery;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllDeliveryOrderResponseDto(
        Long id,
        Long purchaseOrderId,
        Long saleOrderId,
        Instant deliveryDate,
        BigDecimal totalDeliveryFee,
        String deliveryType,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
