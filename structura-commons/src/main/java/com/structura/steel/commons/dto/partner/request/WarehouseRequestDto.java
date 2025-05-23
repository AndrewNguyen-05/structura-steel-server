package com.structura.steel.commons.dto.partner.request;

import jakarta.validation.constraints.NotNull;

public record WarehouseRequestDto (
    @NotNull(message = "Warehouse name must not be null")
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
