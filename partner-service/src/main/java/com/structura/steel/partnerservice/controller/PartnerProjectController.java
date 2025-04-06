package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.commons.utils.AppConstants;
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
        return ResponseEntity.ok(partnerProjectService.createPartnerProject(partnerId, dto));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> updatePartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId,
            @RequestBody PartnerProjectRequestDto dto) {
        return ResponseEntity.ok(partnerProjectService.updatePartnerProject(partnerId, projectId, dto));
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
    public ResponseEntity<ObjectResponse<PartnerProjectResponseDto>> getAllPartnerProjects(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long partnerId
    ) {
        return ResponseEntity.ok(partnerProjectService.getAllPartnerProjectsByPartnerId(pageNo, pageSize, sortBy, sortDir, partnerId));
    }
}
