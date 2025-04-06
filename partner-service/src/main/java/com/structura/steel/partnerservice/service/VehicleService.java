package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;

public interface VehicleService {

    VehicleResponseDto createVehicle(Long partnerId, VehicleRequestDto dto);

    VehicleResponseDto updateVehicle(Long partnerId, Long vehicleId, VehicleRequestDto dto);

    VehicleResponseDto getVehicleById(Long partnerId, Long vehicleId);

    void deleteVehicle(Long partnerId, Long vehicleId);

    ObjectResponse<VehicleResponseDto> getAllVehiclesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId);
}
