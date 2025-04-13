package com.structura.steel.dto.response;

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
    private PartnerResponseDto partner;
    private PartnerProjectResponseDto project;
    private Instant orderDate;
    private UserResponse user;
    private String status;
    private String orderType;
    private BigDecimal totalAmount;
    private String saleOrdersNote;
    private Set<SaleOrderDetailResponseDto> saleOrderDetails;
    private Set<SaleDebtResponseDto> saleDebts;
}
