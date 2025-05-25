package com.structura.steel.commons.dto.core.response;

import com.structura.steel.commons.enumeration.DebtStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryDebtResponseDto(
        Long id,
        Long deliveryOrderId,
        String orderType,
        BigDecimal originalAmount,
        BigDecimal remainingAmount,
        Instant paymentDate,
        Instant paidDate,
        String debtNote,
        DebtStatus status,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
