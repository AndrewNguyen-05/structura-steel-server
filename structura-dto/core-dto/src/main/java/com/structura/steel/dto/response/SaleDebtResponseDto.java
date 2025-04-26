package com.structura.steel.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDebtResponseDto(
        Long id,
        Long saleOrderId,
        Long projectId,
        BigDecimal amount,
        Instant paymentDate,
        Instant paidDate,
        String debtNote,
        String status
) {}
