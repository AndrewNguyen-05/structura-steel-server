package com.structura.steel.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDebtRequestDto(
        Long saleOrderId,
        Long projectId,
        String orderType, // "SALE" hoặc "PURCHASE"
        BigDecimal amount,
        Instant paymentDate,
        Instant paidDate,
        String debtNote,
        String status
) {}
