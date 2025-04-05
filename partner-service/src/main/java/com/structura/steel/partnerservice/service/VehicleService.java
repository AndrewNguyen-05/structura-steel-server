package com.structura.steel.partnerservice.service;

import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;

import java.util.List;

public interface VehicleService {

    VehicleResponseDto createVehicle(Long partnerId, VehicleRequestDto dto);

    VehicleResponseDto updateVehicle(Long partnerId, Long vehicleId, VehicleRequestDto dto);

    VehicleResponseDto getVehicle(Long partnerId, Long vehicleId);

    void deleteVehicle(Long partnerId, Long vehicleId);

    List<VehicleResponseDto> getAllVehiclesByPartnerId(Long partnerId);
}
