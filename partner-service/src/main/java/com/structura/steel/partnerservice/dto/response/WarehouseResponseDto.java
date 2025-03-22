package com.structura.steel.partnerservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseResponseDto {
    private Long id;
    private String warehouseName;
    private String warehouseAddress;
    private Long partnerId;
}
