package com.structura.steel.commons.dto.core.response.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class DeliveryOrderResponseDto {

    private Long id;
    private String status;
    private String deliveryCode;
    private Long purchaseOrderId;
    private Long saleOrderId;
    private Instant deliveryDate;
    private Long vehicleId;
    private String driverName;
    private String deliveryAddress;
    private String confirmationFromPartner;
    private String confirmationFromFactory;
    private BigDecimal distance;
    private BigDecimal deliveryUnitPrice;
    private BigDecimal additionalFees;
    private BigDecimal totalDeliveryFee;
    private String deliveryType;
    private String deliveryOrderNote;

    private Set<DeliveryDebtResponseDto> deliveryDebts;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
