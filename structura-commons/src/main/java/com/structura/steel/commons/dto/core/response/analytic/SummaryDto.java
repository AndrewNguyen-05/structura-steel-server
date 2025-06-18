package com.structura.steel.commons.dto.core.response.analytic;

import java.math.BigDecimal;

public record SummaryDto(
    BigDecimal totalRevenue,
    BigDecimal totalCostOfGoods,
    BigDecimal grossProfit,
    BigDecimal totalReceivable,
    BigDecimal totalPayable
) {}
