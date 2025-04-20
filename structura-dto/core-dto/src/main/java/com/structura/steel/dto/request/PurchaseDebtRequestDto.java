package com.structura.steel.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseDebtRequestDto(
        Long purchaseOrderId,
        Long projectId,
        String orderType,         // "SALE" | "PURCHASE"
        BigDecimal amount,
        Instant paymentDate,
        Instant paidDate,
        String debtNote,
        String status
) {}
