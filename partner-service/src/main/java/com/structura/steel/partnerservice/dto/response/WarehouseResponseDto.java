package com.structura.steel.partnerservice.dto.response;

public record WarehouseResponseDto (
    Long id,
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
