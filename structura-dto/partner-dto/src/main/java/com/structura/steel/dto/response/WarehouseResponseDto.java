package com.structura.steel.dto.response;

import java.time.Instant;

public record WarehouseResponseDto (
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
) {}
