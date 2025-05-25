package com.structura.steel.commons.dto.product.request;

import com.structura.steel.commons.enumeration.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
        String name,

        @Positive(message = "Unit weight must be positive")
        BigDecimal unitWeight,

        @NotNull(message = "Type is mandatory")
        ProductType productType,

        @NotNull(message = "Length cannot be null")
        @Positive(message = "Length must be positive")
        BigDecimal length,

        @Positive(message = "Width must be positive")
        BigDecimal width,

        @Positive(message = "Height must be positive")
        BigDecimal height,

        @Positive(message = "Thickness must be positive")
        BigDecimal thickness,

        @Positive(message = "Diameter must be positive")
        BigDecimal diameter,

        String standard,
        boolean deleted
) {}