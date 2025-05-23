package com.structura.steel.commons.dto.core.response;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseOrderDetailResponseDto(
        Long id,
        Long purchaseOrderId,
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
