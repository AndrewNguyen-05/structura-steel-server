package com.structura.steel.partnerservice.service;

import com.structura.steel.partnerservice.dto.request.VehicleRequestDto;
import com.structura.steel.partnerservice.dto.response.VehicleResponseDto;

import java.util.List;

public interface VehicleService {
    VehicleResponseDto createVehicle(VehicleRequestDto dto);
    VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto);
    VehicleResponseDto getVehicle(Long id);
    void deleteVehicle(Long id);
    List<VehicleResponseDto> getAllVehicles();
}
