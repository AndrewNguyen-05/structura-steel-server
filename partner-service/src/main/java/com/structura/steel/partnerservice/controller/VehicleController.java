package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.dto.partner.request.VehicleRequestDto;
import com.structura.steel.commons.dto.partner.response.VehicleResponseDto;
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
        return ResponseEntity.ok(vehicleService.createVehicle(partnerId, dto));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId,
            @RequestBody VehicleRequestDto dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(partnerId, vehicleId, dto));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicleByPartnerId(partnerId, vehicleId));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(partnerId, vehicleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft-delete/{vehicleId}") // New: Soft delete endpoint
    public ResponseEntity<Void> softDeleteVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        vehicleService.softDeleteVehicle(partnerId, vehicleId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{vehicleId}") // New: Restore endpoint
    public ResponseEntity<VehicleResponseDto> restoreVehicle(
            @PathVariable Long partnerId,
            @PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.restoreVehicle(partnerId, vehicleId));
    }


    @GetMapping
    public ResponseEntity<PagingResponse<VehicleResponseDto>> getAllVehicles(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "deleted", defaultValue = AppConstants.DELETED, required = false) boolean deleted,
            @PathVariable Long partnerId,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {
        return ResponseEntity.ok(vehicleService.getAllVehiclesByPartnerId(pageNo, pageSize, sortBy, sortDir, partnerId, searchKeyword, deleted));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam("prefix") String prefix,
            @PathVariable Long partnerId,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "deleted", defaultValue = AppConstants.DELETED, required = false) boolean deleted
    ) {
        return ResponseEntity.ok(vehicleService.suggestVehicles(prefix, size, deleted, partnerId));
    }
}
