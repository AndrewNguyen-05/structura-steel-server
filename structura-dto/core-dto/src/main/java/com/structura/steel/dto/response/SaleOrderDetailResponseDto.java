package com.structura.steel.dto.response;

import java.math.BigDecimal;

public record SaleOrderDetailResponseDto(
        Long id,
        Long saleOrderId,
        Long productId,
        BigDecimal quantity,
        BigDecimal weight,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
