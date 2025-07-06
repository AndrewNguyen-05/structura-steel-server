package com.structura.steel.commons.dto.core.response.delivery;

import com.structura.steel.commons.enumeration.ConfirmationStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllDeliveryOrderResponseDto(
        Long id,
        String status,
		String deliveryCode,
        ConfirmationStatus confirmationFromPartner,
		ConfirmationStatus confirmationFromFactory,
		ConfirmationStatus confirmationFromReceiver,
        Long purchaseOrderId,
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
