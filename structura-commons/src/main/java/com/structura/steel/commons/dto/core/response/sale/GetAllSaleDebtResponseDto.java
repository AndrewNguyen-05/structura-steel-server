package com.structura.steel.commons.dto.core.response.sale;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllSaleDebtResponseDto (
    Long id,
    Long productId,
    BigDecimal amount,
    String debtNote,
    String status,
    Short version,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy
) {}
