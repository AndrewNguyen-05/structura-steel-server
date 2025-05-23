package com.structura.steel.commons.dto.core.request;

import java.math.BigDecimal;

public record PurchaseOrderDetailRequestDto(
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {}
