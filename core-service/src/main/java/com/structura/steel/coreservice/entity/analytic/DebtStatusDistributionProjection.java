package com.structura.steel.coreservice.entity.analytic;

public interface DebtStatusDistributionProjection {
	com.structura.steel.commons.enumeration.DebtStatus getStatus();
	long getCount();
	java.math.BigDecimal getTotalAmount();
}
