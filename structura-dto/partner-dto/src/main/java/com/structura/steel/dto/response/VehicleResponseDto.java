package com.structura.steel.dto.response;

import java.time.Instant;

public record VehicleResponseDto (
    Long id,
    String vehicleType,
    String licensePlate,
	String vehicleCode,
    Double capacity,
    String description,
    String driverName,
    Long partnerId,
    Short version,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy
) {}
