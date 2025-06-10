package com.structura.steel.commons.dto.core.request.delivery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryOrderRequestDto(

        Long purchaseOrderId,

        Long saleOrderId,

        @NotNull
        @NotEmpty(message = "Must contain delivery date")
        Instant deliveryDate,

        @NotNull
        @NotEmpty(message = "Must contain vehicle")
        Long vehicleId,
        Long partnerId,

        @NotNull
        @NotEmpty(message = "Must contain driver name")
        String driverName,

        @NotNull
        @NotEmpty(message = "Must contain delivery address")
        String deliveryAddress,

        @NotNull
        @NotEmpty(message = "Must contain distance")
        BigDecimal distance,

        @NotNull
        @NotEmpty(message = "Must contain total delivery unit price")
        BigDecimal deliveryUnitPrice,

        BigDecimal additionalFees,

        String deliveryOrderNote
) {}
