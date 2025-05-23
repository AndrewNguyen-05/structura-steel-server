package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.dto.partner.request.WarehouseRequestDto;
import com.structura.steel.commons.dto.partner.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners/{partnerId}/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @PathVariable Long partnerId,
            @RequestBody WarehouseRequestDto dto) {
        return ResponseEntity.ok(warehouseService.createWarehouse(partnerId, dto));
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> updateWarehouse(
            @PathVariable Long partnerId,
            @PathVariable Long warehouseId,
            @RequestBody WarehouseRequestDto dto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(partnerId, warehouseId, dto));
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> getWarehouse(
            @PathVariable Long partnerId,
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouse(partnerId, warehouseId));
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(
            @PathVariable Long partnerId,
            @PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(partnerId, warehouseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagingResponse<WarehouseResponseDto>> getAllWarehouses(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long partnerId,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesByPartnerId(pageNo, pageSize, sortBy, sortDir, partnerId, searchKeyword));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam("prefix") String prefix,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(warehouseService.suggestWarehouses(prefix, size));
    }
}
