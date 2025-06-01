package com.structura.steel.commons.dto.core.request.sale;


public record SaleOrderRequestDto(
        Long partnerId,
        Long projectId,
        String saleOrdersNote
) {}

