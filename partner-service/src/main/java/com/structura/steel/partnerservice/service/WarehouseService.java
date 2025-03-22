package com.structura.steel.partnerservice.service;

import com.structura.steel.partnerservice.dto.request.WarehouseRequestDto;
import com.structura.steel.partnerservice.dto.response.WarehouseResponseDto;

import java.util.List;

public interface WarehouseService {
    WarehouseResponseDto createWarehouse(WarehouseRequestDto dto);
    WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto dto);
    WarehouseResponseDto getWarehouse(Long id);
    void deleteWarehouse(Long id);
    List<WarehouseResponseDto> getAllWarehouses();
}
