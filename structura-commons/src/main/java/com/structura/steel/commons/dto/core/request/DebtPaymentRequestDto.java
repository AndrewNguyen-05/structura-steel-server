package com.structura.steel.commons.dto.core.request;

import com.structura.steel.commons.enumeration.DebtType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record DebtPaymentRequestDto(
        @NotNull
        DebtType debtType,

        @NotNull
        Long debtId, // ID of PurchaseDebt, SaleDebt, or DeliveryDebt


        @NotNull(message = "Payment date cannot be null")
        Instant paymentDate,

        @NotNull
        @Positive(message = "Payment amount must be positive")
        BigDecimal amountPaid,

        String paymentMethod,
        String notes
) { }
