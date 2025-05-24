package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.partner.request.WarehouseRequestDto;
import com.structura.steel.commons.dto.partner.response.WarehouseResponseDto;

import java.util.List;

public interface WarehouseService {

    WarehouseResponseDto createWarehouse(Long partnerId, WarehouseRequestDto dto);

    WarehouseResponseDto updateWarehouse(Long partnerId, Long warehouseId, WarehouseRequestDto dto);

    WarehouseResponseDto getWarehouse(Long partnerId, Long warehouseId);

    void deleteWarehouse(Long partnerId, Long warehouseId);

    void softDeleteWarehouse(Long partnerId, Long warehouseId);

    WarehouseResponseDto restoreWarehouse(Long partnerId, Long warehouseId);

    PagingResponse<WarehouseResponseDto> getAllWarehousesByPartnerId(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            Long partnerId,
            String searchKeyword,
            boolean deleted
    );

    List<String> suggestWarehouses(
            String prefix,
            int size,
            boolean deleted,
            Long partnerId
    );
}
