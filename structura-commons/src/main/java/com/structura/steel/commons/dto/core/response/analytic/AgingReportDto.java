package com.structura.steel.commons.dto.core.response.analytic;

public record AgingReportDto(
		String partnerName,
		java.math.BigDecimal totalDue,
		java.math.BigDecimal current,     // 0-30 ngày
		java.math.BigDecimal dueIn31_60,  // 31-60 ngày
		java.math.BigDecimal dueIn61_90,  // 61-90 ngày
		java.math.BigDecimal over90      // >90 ngày
) {}