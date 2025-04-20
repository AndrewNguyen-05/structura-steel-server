package com.structura.steel.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseOrderResponseDto {

    private Long id;
    private PartnerResponseDto supplier;           // Partner service
    private PartnerProjectResponseDto project;     // Partner‑project
    private Instant orderDate;
    private UserResponse user;
    private String status;
    private BigDecimal totalAmount;
    private String purchaseOrdersNote;

    private Set<PurchaseOrderDetailResponseDto> purchaseOrderDetails;
    private Set<PurchaseDebtResponseDto> purchaseDebts;
}
