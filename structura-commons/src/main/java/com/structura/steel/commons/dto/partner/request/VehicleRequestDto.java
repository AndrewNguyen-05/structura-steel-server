package com.structura.steel.commons.dto.partner.request;

import com.structura.steel.commons.enumeration.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleRequestDto (
        @NotNull(message = "Vehicle type must not be null")
        VehicleType vehicleType,

        @NotBlank(message = "License plate must not be empty")
        String licensePlate,

        @NotNull(message = "Capacity cannot be null")
        @Positive(message = "Capacity must be positive")
        Double capacity,
        String description,
        String driverName
) {}
