package com.structura.steel.partnerservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VehicleRequestDto {
    private String vehicleType;
    private String licensePlate;
    private BigDecimal capacity;
    private String description;
    private String driverName;
    private Long partnerId;
}
