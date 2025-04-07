package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;

public interface WarehouseService {

    WarehouseResponseDto createWarehouse(Long partnerId, WarehouseRequestDto dto);

    WarehouseResponseDto updateWarehouse(Long partnerId, Long warehouseId, WarehouseRequestDto dto);

    WarehouseResponseDto getWarehouse(Long partnerId, Long warehouseId);

    void deleteWarehouse(Long partnerId, Long warehouseId);

    PagingResponse<WarehouseResponseDto> getAllWarehousesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId);
}
