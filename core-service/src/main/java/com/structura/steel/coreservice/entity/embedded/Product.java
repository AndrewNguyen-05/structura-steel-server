package com.structura.steel.coreservice.entity.embedded;

import com.structura.steel.commons.enumeration.ProductType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record Product (
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
        BigDecimal importPrice,
        BigDecimal exportPrice,
        BigDecimal profitPercentage,
        String standard,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) implements Serializable {}
