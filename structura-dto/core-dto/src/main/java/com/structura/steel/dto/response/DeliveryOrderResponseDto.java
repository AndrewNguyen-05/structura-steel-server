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
public class DeliveryOrderResponseDto {

    private Long id;
    private Long purchaseOrderId;
    private Long saleOrderId;
    private Instant deliveryDate;
    private Long vehicleId;
    private String driverName;
    private Long warehouseId;
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
}
