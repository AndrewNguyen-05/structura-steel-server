package com.structura.steel.commons.dto.core.response;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseDebtResponseDto(
        Long id,
        Long purchaseOrderId,
        Long projectId,
        String orderType,
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
