package com.structura.steel.commons.dto.core.response.purchase;

import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.enumeration.ConfirmationStatus;
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
    private String status;
    private String importCode;
    private ConfirmationStatus confirmationFromSupplier;
    private PartnerResponseDto supplier;
    private PartnerProjectResponseDto project;
    private BigDecimal totalAmount;
    private String purchaseOrdersNote;

    private Set<PurchaseOrderDetailResponseDto> purchaseOrderDetails;
    private Set<PurchaseDebtResponseDto> purchaseDebts;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
