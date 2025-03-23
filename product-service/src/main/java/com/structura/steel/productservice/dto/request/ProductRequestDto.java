package com.structura.steel.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Code is mandatory")
        @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
        String code,

        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
        String name,

        BigDecimal unitWeight,

        @NotNull(message = "Length cannot be null")
        BigDecimal length,

        BigDecimal width,
        BigDecimal height,
        BigDecimal thickness,
        BigDecimal diameter,
        String standard
) {}
