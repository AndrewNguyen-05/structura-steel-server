package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.VehicleResponseDto;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import com.structura.steel.partnerservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartnerDetailController {

    private final PartnerProjectService partnerProjectService;
    private final VehicleService vehicleService;

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> getProjectById(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.ok(partnerProjectService.getProjectById(projectId));
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(@PathVariable("vehicleId") Long vehicleId) {

        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

}
