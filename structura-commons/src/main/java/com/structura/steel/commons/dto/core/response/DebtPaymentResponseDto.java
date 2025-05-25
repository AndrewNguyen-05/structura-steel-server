package com.structura.steel.commons.dto.core.response;

import com.structura.steel.commons.enumeration.DebtType;

import java.math.BigDecimal;
import java.time.Instant;

public record DebtPaymentResponseDto(
        Long id,
        DebtType debtType,
        Long debtId,
        BigDecimal amountPaid,
        Instant paymentDate,
        String paymentMethod,
        String notes,
        Instant createdAt,
        String createdBy
) { }
