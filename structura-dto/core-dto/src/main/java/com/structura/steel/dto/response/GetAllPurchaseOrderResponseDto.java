package com.structura.steel.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAllPurchaseOrderResponseDto(
        Long id,
        Long supplierId,
        Long projectId,
        Instant orderDate,
        String status,
        BigDecimal totalAmount,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
