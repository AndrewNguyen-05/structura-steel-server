package com.structura.steel.commons.dto.core.request;

import java.math.BigDecimal;

public record SaleOrderDetailRequestDto(
        Long productId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {}
