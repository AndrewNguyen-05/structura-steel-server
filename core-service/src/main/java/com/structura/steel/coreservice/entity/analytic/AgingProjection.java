package com.structura.steel.coreservice.entity.analytic;

public interface AgingProjection {
	String getPartnerName();
	java.math.BigDecimal getRemainingAmount();
	java.time.Instant getCreatedAt();
}
