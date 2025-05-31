package com.structura.steel.commons.dto.core.response.sale;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllSaleOrderDetailResponseDto(
    Long id,
    Long productId,
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