package com.structura.steel.commons.dto.core.request.purchase;

public record PurchaseOrderRequestDto(
        Long supplierId,
        Long saleOrderId,
        Long projectId,
        String purchaseOrdersNote
) {}
