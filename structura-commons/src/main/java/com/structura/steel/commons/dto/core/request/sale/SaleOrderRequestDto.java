package com.structura.steel.commons.dto.core.request.sale;


public record SaleOrderRequestDto(
        Long partnerId,
        Long projectId,
        Long warehouseId,
        String saleOrdersNote
) {}

