package com.structura.steel.commons.dto.core.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class GetAllPurchaseOrderResponseDto {
	private Long id;
	private Long supplierId;
	private String supplierName;
	private Long projectId;
	private String projectName;
	private Instant orderDate;
	private String status;
	private BigDecimal totalAmount;
	private Short version;
	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;
}
