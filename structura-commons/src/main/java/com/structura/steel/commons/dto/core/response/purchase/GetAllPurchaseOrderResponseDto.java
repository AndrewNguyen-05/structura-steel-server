package com.structura.steel.commons.dto.core.response.purchase;

import com.structura.steel.commons.enumeration.ConfirmationStatus;
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
	private String status;
	private String importCode;
	private Long supplierId;
	private ConfirmationStatus confirmationFromSupplier;
	private String supplierName;
	private String supplierCode;
	private Long projectId;
	private String projectName;
	private String projectCode;
	private BigDecimal totalAmount;
	private Short version;
	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;
}
