package com.structura.steel.partnerservice.dto.request;

public record VehicleRequestDto (
    String vehicleType,
    String licensePlate,
    Double capacity,
    String description,
    String driverName,
    Long partnerId
) {}
