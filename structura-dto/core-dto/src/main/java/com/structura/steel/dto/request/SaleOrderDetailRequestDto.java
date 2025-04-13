package com.structura.steel.dto.request;

import java.math.BigDecimal;

public record SaleOrderDetailRequestDto(
        Long saleOrderId,
        Long productId,
        BigDecimal quantity,
        BigDecimal weight,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
