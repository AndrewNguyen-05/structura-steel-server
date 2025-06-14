package com.structura.steel.commons.dto.core.request.delivery;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DeliveryDebtRequestDto(

        Long deliveryOrderId,

        @NotNull(message = "Original amount cannot be null")
        @Positive(message = "Original amount must be positive")
        BigDecimal originalAmount,

        String debtNote
) {}
