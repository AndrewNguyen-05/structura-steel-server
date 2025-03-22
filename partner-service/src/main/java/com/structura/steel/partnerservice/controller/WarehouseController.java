package com.structura.steel.partnerservice.controller;

import com.structura.steel.partnerservice.dto.request.WarehouseRequestDto;
import com.structura.steel.partnerservice.dto.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouse(id));
    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(@RequestBody WarehouseRequestDto dto) {
        WarehouseResponseDto created = warehouseService.createWarehouse(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> updateWarehouse(@PathVariable Long id,
                                                                @RequestBody WarehouseRequestDto dto) {
        WarehouseResponseDto updated = warehouseService.updateWarehouse(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
