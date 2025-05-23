package com.structura.steel.commons.dto.partner.response;

import com.structura.steel.commons.enumeration.VehicleType;

import java.time.Instant;

public record VehicleResponseDto (
		Long id,
    	VehicleType vehicleType,
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
