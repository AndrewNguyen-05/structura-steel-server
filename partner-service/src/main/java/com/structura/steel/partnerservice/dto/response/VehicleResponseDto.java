package com.structura.steel.partnerservice.dto.response;

public record VehicleResponseDto (
    Long id,
    String vehicleType,
    String licensePlate,
    Double capacity,
    String description,
    String driverName,
    Long partnerId
) {}
