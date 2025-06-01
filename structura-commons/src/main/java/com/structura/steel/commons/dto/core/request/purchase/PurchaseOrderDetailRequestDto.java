package com.structura.steel.commons.dto.core.request.purchase;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PurchaseOrderDetailRequestDto(
        Long productId,

        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @NotNull(message = "Unit price cannot be null")
        @Positive(message = "Unit price must be positive")
        BigDecimal unitPrice
) {}
