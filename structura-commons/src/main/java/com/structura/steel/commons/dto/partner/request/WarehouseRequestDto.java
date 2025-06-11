package com.structura.steel.commons.dto.partner.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WarehouseRequestDto (

    @NotBlank(message = "Warehouse name must not be empty")
    String warehouseName,
    String warehouseAddress,
    Long partnerId
) {}
