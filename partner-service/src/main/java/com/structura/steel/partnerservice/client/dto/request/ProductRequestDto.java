package com.structura.steel.partnerservice.client.dto.request;

import java.math.BigDecimal;

public record ProductRequestDto(
    String code,
    String name,
    BigDecimal unitWeight,
    BigDecimal length,
    BigDecimal width,
    BigDecimal height,
    BigDecimal thickness,
    BigDecimal diameter,
    String standard
) { }
