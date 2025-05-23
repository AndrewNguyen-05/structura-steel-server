package com.structura.steel.commons.dto.core.request;

import java.math.BigDecimal;
import java.time.Instant;

public record DeliveryDebtRequestDto(
        Long deliveryOrderId,
        String orderType,             // "SALE" | "PURCHASE"
        BigDecimal amount,
        Instant paymentDate,
        Instant paidDate,
        String debtNote,
        String status
) {}
