package com.structura.steel.commons.dto.core.response.sale;

import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class SaleOrderResponseDto{
    private Long id;
    private String status;
    private String exportCode;
    private PartnerResponseDto partner;
    private PartnerProjectResponseDto project;
    private Long warehouseId;
    private BigDecimal totalAmount;
    private String saleOrdersNote;
    private Set<SaleOrderDetailResponseDto> saleOrderDetails;
    private Set<SaleDebtResponseDto> saleDebts;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
