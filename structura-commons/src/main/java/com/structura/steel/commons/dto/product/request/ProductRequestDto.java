package com.structura.steel.commons.dto.product.request;

import com.structura.steel.commons.enumeration.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
        String name,

        BigDecimal unitWeight,

        @NotNull(message = "Type is mandatory")
        ProductType productType,

        @NotNull(message = "Length cannot be null")
        BigDecimal length,

        BigDecimal width,
        BigDecimal height,
        BigDecimal thickness,
        BigDecimal diameter,
        String standard,
        boolean deleted
) {}
