package com.structura.steel.commons.dto.core.request.delivery;

import com.structura.steel.commons.enumeration.ConfirmationStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateDeliveryOrderRequestDto(

        ConfirmationStatus confirmationFromFactory,
        ConfirmationStatus confirmationFromPartner,
        ConfirmationStatus confirmationFromReceiver,

        @NotNull
        @NotEmpty(message = "Must contain driver name")
        String driverName,

        @NotNull
        @NotEmpty(message = "Must contain sender address")
        String senderAddress,

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
