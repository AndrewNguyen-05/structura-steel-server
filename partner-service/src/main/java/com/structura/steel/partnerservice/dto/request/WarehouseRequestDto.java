package com.structura.steel.partnerservice.dto.request;

public record WarehouseRequestDto (
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
