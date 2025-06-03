package com.structura.steel.commons.dto.core.response.sale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class GetAllSaleOrderResponseDto {
    private Long id;
    private Long partnerId;
	private String partnerName;
    private String partnerCode;
    private Long projectId;
	private String projectName;
    private String projectCode;
    private String status;
    private BigDecimal totalAmount;
    private String saleOrdersNote;
    private Set<SaleOrderDetailResponseDto>saleOrderDetails;
    private Set<SaleDebtResponseDto> saleDebts;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
