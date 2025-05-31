package com.structura.steel.coreservice.entity.embedded;

import java.io.Serializable;
import java.time.Instant;

public record Warehouse (
        Long id,
        String warehouseName,
        String warehouseCode,
        String warehouseAddress,
        Long partnerId,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) implements Serializable {}
