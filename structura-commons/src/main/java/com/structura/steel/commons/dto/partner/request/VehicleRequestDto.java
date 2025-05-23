package com.structura.steel.commons.dto.partner.request;

import jakarta.validation.constraints.NotNull;

public record VehicleRequestDto (
    String vehicleType,

    @NotNull(message = "License plate must not be null")
    String licensePlate,
    Double capacity,
    String description,
    String driverName,
    Long partnerId
) {}
