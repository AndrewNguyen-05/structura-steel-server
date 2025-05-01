package com.structura.steel.dto.response;

import java.math.BigDecimal;

public record GetAllSaleOrderDetailResponseDto(
    Long id,
    Long productId,
    BigDecimal quantity,
    BigDecimal weight,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}