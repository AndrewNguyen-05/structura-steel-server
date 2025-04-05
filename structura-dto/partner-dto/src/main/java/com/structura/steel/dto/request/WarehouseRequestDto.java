package com.structura.steel.dto.request;

import jakarta.validation.constraints.NotNull;

public record WarehouseRequestDto (
    @NotNull(message = "Warehouse name must not be null")
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
