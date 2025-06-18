package com.structura.steel.coreservice.entity.analytic;

public interface RevenueOverTimeProjection {
    String getDateLabel();
    java.math.BigDecimal getTotalRevenue();
}