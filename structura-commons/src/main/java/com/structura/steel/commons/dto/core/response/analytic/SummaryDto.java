package com.structura.steel.commons.dto.core.response.analytic;

import java.math.BigDecimal;

public record SummaryDto(
    BigDecimal totalRevenue,
    BigDecimal totalCostOfGoods,
	BigDecimal totalDeliveryCost,
    BigDecimal grossProfit,
    BigDecimal totalDebtReceivable,
    BigDecimal totalDebtPayable
) {}
