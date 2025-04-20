package com.structura.steel.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseOrderRequestDto(
        Long supplierId,
        Long projectId,
        Instant orderDate,
        String userId,
        String status,
        BigDecimal totalAmount,
        String purchaseOrdersNote
) {}
