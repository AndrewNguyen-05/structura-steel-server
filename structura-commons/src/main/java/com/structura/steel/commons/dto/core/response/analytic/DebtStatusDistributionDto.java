package com.structura.steel.commons.dto.core.response.analytic;

public record DebtStatusDistributionDto(
		String status,
		long count,
		java.math.BigDecimal totalAmount
) {}
