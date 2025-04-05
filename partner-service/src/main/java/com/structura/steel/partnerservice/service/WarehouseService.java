package com.structura.steel.partnerservice.service;

import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;

import java.util.List;

public interface WarehouseService {

    WarehouseResponseDto createWarehouse(Long partnerId, WarehouseRequestDto dto);

    WarehouseResponseDto updateWarehouse(Long partnerId, Long warehouseId, WarehouseRequestDto dto);

    WarehouseResponseDto getWarehouse(Long partnerId, Long warehouseId);

    void deleteWarehouse(Long partnerId, Long warehouseId);

    List<WarehouseResponseDto> getAllWarehousesByPartnerId(Long partnerId);
}
