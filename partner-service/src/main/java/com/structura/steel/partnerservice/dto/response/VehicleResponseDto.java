package com.structura.steel.partnerservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VehicleResponseDto {
    private Long id;
    private String vehicleType;
    private String licensePlate;
    private BigDecimal capacity;
    private String description;
    private String driverName;
    private Long partnerId;
}
