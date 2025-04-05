package com.structura.steel.dto.response;

public record VehicleResponseDto (
    Long id,
    String vehicleType,
    String licensePlate,
    Double capacity,
    String description,
    String driverName,
    Long partnerId
) {}
