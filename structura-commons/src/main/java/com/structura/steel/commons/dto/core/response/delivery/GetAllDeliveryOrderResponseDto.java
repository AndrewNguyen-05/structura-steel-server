package com.structura.steel.commons.dto.core.response.delivery;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllDeliveryOrderResponseDto(
        Long id,
        String status,
		String deliveryCode,
        String deliveryType,
        String confirmationFromPartner,
        String confirmationFromFactory,
        String confirmationFromReceiver,
        Long purchaseOrderId,
        Long saleOrderId,
        Instant deliveryDate,
		BigDecimal deliveryUnitPrice,
		BigDecimal additionalFees,
        BigDecimal totalDeliveryFee,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
