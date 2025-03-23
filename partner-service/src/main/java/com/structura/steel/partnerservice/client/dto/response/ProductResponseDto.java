package com.structura.steel.partnerservice.client.dto.response;

import java.math.BigDecimal;

public record ProductResponseDto(
    Long id,
    String code,
    String name,
    BigDecimal unitWeight,
    BigDecimal length,
    BigDecimal width,
    BigDecimal height,
    BigDecimal thickness,
    BigDecimal diameter,
    String standard
) {}
