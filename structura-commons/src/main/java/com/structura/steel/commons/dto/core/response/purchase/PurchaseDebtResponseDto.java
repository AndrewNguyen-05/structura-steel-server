package com.structura.steel.commons.dto.core.response.purchase;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseDebtResponseDto {
        private Long id;
        private String status;
        private Long purchaseOrderId;
        private ProductResponseDto product;
        private BigDecimal originalAmount;
        private BigDecimal remainingAmount;
        private String debtNote;
        private Short version;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;
}
