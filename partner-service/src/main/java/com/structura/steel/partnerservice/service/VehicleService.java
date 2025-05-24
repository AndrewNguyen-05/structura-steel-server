package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.partner.request.VehicleRequestDto;
import com.structura.steel.commons.dto.partner.response.VehicleResponseDto;

import java.util.List;

public interface VehicleService {

    VehicleResponseDto createVehicle(Long partnerId, VehicleRequestDto dto);

    VehicleResponseDto updateVehicle(Long partnerId, Long vehicleId, VehicleRequestDto dto);

    VehicleResponseDto getVehicleById(Long partnerId, Long vehicleId);

    void deleteVehicle(Long partnerId, Long vehicleId);

    void softDeleteVehicle(Long partnerId, Long vehicleId);

    VehicleResponseDto restoreVehicle(Long partnerId, Long vehicleId);

    PagingResponse<VehicleResponseDto> getAllVehiclesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword, boolean deleted);

    List<String> suggestVehicles(String prefix, int size, boolean deleted, Long partnerId);
}
