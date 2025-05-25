package com.structura.steel.commons.dto.core.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryDebtRequestDto(
        Long deliveryOrderId,
        String orderType, // "SALE" | "PURCHASE"

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        String debtNote,
        String status
) {}
