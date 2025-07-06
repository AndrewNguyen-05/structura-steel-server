package com.structura.steel.commons.dto.core.response.sale;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllSaleOrderDetailResponseDto(
    Long id,
    Long productId,
    ProductResponseDto product,
    BigDecimal quantity,
    BigDecimal weight,
    BigDecimal unitPrice,
    BigDecimal subtotal,
    Short version,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy
) {}