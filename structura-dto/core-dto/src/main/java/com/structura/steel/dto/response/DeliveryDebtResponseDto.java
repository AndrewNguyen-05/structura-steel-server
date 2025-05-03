package com.structura.steel.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryDebtResponseDto(
        Long id,
        Long deliveryOrderId,
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
