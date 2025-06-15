package com.structura.steel.commons.dto.core.response.report;

import com.structura.steel.commons.enumeration.DebtType;

import java.math.BigDecimal;
import java.time.Instant;

public record PayableReportDto(
        String partnerName,
        DebtType debtType,
        String referenceCode,
        Instant debtDate,
        BigDecimal remainingAmount
) {}
