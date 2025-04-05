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
        WarehouseResponseDto created = warehouseService.createWarehouse(partnerId, dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> updateWarehouse(
            @PathVariable Long partnerId,
            @PathVariable Long warehouseId,
            @RequestBody WarehouseRequestDto dto) {
        WarehouseResponseDto updated = warehouseService.updateWarehouse(partnerId, warehouseId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> getWarehouse(
            @PathVariable Long partnerId,
            @PathVariable Long warehouseId) {
        WarehouseResponseDto dto = warehouseService.getWarehouse(partnerId, warehouseId);
        return ResponseEntity.ok(dto);
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
        List<WarehouseResponseDto> dtos = warehouseService.getAllWarehousesByPartnerId(partnerId);
        return ResponseEntity.ok(dtos);
    }
}
