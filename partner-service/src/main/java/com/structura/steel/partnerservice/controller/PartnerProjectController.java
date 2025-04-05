package com.structura.steel.partnerservice.controller;

import com.structura.steel.dto.request.PartnerProjectRequestDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners/{partnerId}/projects")
@RequiredArgsConstructor
public class PartnerProjectController {

    private final PartnerProjectService partnerProjectService;

    @PostMapping
    public ResponseEntity<PartnerProjectResponseDto> createPartnerProject(
            @PathVariable Long partnerId,
            @RequestBody PartnerProjectRequestDto dto) {
        PartnerProjectResponseDto created = partnerProjectService.createPartnerProject(partnerId, dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> updatePartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId,
            @RequestBody PartnerProjectRequestDto dto) {
        PartnerProjectResponseDto updated = partnerProjectService.updatePartnerProject(partnerId, projectId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> getPartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId) {
        PartnerProjectResponseDto dto = partnerProjectService.getPartnerProject(partnerId, projectId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deletePartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId) {
        partnerProjectService.deletePartnerProject(partnerId, projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PartnerProjectResponseDto>> getAllPartnerProjects(
            @PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerProjectService.getAllPartnerProjectsByPartnerId(partnerId));
    }
}
