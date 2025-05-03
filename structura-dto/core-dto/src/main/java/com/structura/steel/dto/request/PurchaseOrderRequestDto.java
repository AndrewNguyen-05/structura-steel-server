package com.structura.steel.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseOrderRequestDto(
        Long projectId,
        Instant orderDate,
        String status,
        BigDecimal totalAmount,
        String purchaseOrdersNote
) {}
