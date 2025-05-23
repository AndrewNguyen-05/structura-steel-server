package com.structura.steel.commons.dto.core.request;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseOrderRequestDto(
        Long supplierId,
        Long projectId,
        Instant orderDate,
        String status,
        String purchaseOrdersNote
) {}
