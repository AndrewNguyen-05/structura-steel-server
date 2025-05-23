package com.structura.steel.commons.dto.product.response;

import com.structura.steel.commons.enumeration.ProductType;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDto (
    Long id,
    String code,
    String name,
    BigDecimal unitWeight,
	ProductType productType,
	BigDecimal length,
    BigDecimal width,
    BigDecimal height,
    BigDecimal thickness,
    BigDecimal diameter,
    String standard,
    Short version,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy
) {}
