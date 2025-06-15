package com.structura.steel.commons.dto.core.response.report;

import java.math.BigDecimal;
import java.time.Instant;

public record ProfitLossReportDto(
        String saleOrderCode,
        String customerName,
        Instant completionDate,
        BigDecimal revenue,
        BigDecimal costOfGoods,
        BigDecimal deliveryCost,
        BigDecimal grossProfit
) {}