package com.structura.steel.commons.dto.core.response.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDebtResponseDto {

    private Long id;
    private String status;
    private Long deliveryOrderId;
    private String orderType;
    private BigDecimal originalAmount;
    private BigDecimal remainingAmount;
    private String debtNote;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

}