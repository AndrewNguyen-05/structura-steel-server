package com.structura.steel.dto.request;

import java.math.BigDecimal;

public record PurchaseOrderDetailRequestDto(
        Long purchaseOrderId,
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
