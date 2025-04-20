package com.structura.steel.dto.response;

import java.math.BigDecimal;

public record PurchaseOrderDetailResponseDto(
        Long id,
        Long purchaseOrderId,
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
