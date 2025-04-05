package com.structura.steel.dto.response;

public record WarehouseResponseDto (
    Long id,
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
