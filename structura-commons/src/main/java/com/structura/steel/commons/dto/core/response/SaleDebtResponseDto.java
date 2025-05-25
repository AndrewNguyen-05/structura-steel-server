package com.structura.steel.commons.dto.core.response;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.enumeration.DebtStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class SaleDebtResponseDto {
    private Long id;
    private Long saleOrderId;
    private Long productId;
    private ProductResponseDto product;
    private BigDecimal originalAmount;
    private BigDecimal remainingAmount;
    private String debtNote;
    private DebtStatus status;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
};