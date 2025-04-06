package com.structura.steel.partnerservice.controller;

import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;
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
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehouses(
            @PathVariable Long partnerId) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesByPartnerId(partnerId));
    }
}
