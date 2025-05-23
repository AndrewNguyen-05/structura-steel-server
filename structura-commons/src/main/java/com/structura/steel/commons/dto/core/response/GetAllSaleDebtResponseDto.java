package com.structura.steel.commons.dto.core.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllSaleDebtResponseDto (
    Long id,
    Long productId,
    BigDecimal amount,
    Instant paymentDate,
    Instant paidDate,
    String debtNote,
    String status,
    Short version,
    Instant createdAt,
    Instant updatedAt,
    String createdBy,
    String updatedBy
) {}
