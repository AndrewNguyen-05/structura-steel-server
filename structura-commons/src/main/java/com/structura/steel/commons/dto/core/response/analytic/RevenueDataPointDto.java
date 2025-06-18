package com.structura.steel.commons.dto.core.response.analytic;

import java.math.BigDecimal;

public record RevenueDataPointDto(
        String label, // VD: "18-06-2025"
        BigDecimal value // VD: 125000000
) {}
