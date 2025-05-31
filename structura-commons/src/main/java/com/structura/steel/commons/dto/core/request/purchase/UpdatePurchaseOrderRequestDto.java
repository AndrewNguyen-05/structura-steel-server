package com.structura.steel.commons.dto.core.request.purchase;

import com.structura.steel.commons.enumeration.ConfirmationStatus;

public record UpdatePurchaseOrderRequestDto(
        Long warehouseId,
        ConfirmationStatus confirmationFromSupplier,
        String purchaseOrdersNote
) {}
