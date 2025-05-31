package com.structura.steel.coreservice.entity.embedded;


import com.structura.steel.commons.enumeration.VehicleType;

import java.io.Serializable;
import java.time.Instant;

public record Vehicle (
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
) implements Serializable {}