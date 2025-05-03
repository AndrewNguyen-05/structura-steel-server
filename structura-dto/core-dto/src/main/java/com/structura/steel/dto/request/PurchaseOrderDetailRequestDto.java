package com.structura.steel.dto.request;

import java.math.BigDecimal;

public record PurchaseOrderDetailRequestDto(
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {}
