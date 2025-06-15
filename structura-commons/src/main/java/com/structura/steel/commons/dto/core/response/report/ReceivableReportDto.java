package com.structura.steel.commons.dto.core.response.report;

import com.structura.steel.commons.enumeration.DebtStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ReceivableReportDto(
        String customerName,
        String orderCode,
        Instant debtDate,
        BigDecimal remainingAmount,
        DebtStatus status
) {}