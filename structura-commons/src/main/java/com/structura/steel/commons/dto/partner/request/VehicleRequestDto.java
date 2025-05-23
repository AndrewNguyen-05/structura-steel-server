package com.structura.steel.commons.dto.partner.request;

import com.structura.steel.commons.enumeration.VehicleType;
import jakarta.validation.constraints.NotNull;

public record VehicleRequestDto (
        @NotNull(message = "Vehicle type must not be null")
        VehicleType vehicleType,

        @NotNull(message = "License plate must not be null")
        String licensePlate,
        Double capacity,
        String description,
        String driverName,
        Long partnerId
) {}
