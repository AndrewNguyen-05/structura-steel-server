package com.structura.steel.partnerservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseRequestDto {
    private String warehouseName;
    private String warehouseAddress;
    private Long partnerId;
}
