package com.structura.steel.commons.dto.core.request.purchase;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdatePurchaseDebtRequestDto(

        @NotNull(message = "Original amount cannot be null")
        @Positive(message = "Original amount must be positive")
        BigDecimal originalAmount,

        String debtNote,
        String status
) {}
