package com.structura.steel.partnerservice.controller;

import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;
import com.structura.steel.partnerservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners/{partnerId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @PathVariable Long partnerId,
            @RequestBody VehicleRequestDto dto) {
        VehicleResponseDto created = vehicleService.createVehicle(partnerId, dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId,
            @RequestBody VehicleRequestDto dto) {
        VehicleResponseDto updated = vehicleService.updateVehicle(partnerId, vehicleId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> getVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        VehicleResponseDto dto = vehicleService.getVehicle(partnerId, vehicleId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(partnerId, vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicles(
            @PathVariable Long partnerId) {
        List<VehicleResponseDto> dtos = vehicleService.getAllVehiclesByPartnerId(partnerId);
        return ResponseEntity.ok(dtos);
    }
}
