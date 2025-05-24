package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.dto.partner.request.PartnerProjectRequestDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
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

    @DeleteMapping("/soft-delete/{projectId}")
    public ResponseEntity<Void> softDelete(@PathVariable Long partnerId, @PathVariable Long projectId) {
        partnerProjectService.softDeletePartnerProject(partnerId, projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> restore(
            @PathVariable Long partnerId, @PathVariable Long projectId) {
        return ResponseEntity.ok(partnerProjectService.restorePartnerProject(partnerId, projectId));
    }

    @GetMapping
    public ResponseEntity<PagingResponse<PartnerProjectResponseDto>> list(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean deleted,
            @PathVariable Long partnerId
    ) {
        return ResponseEntity.ok(
                partnerProjectService.getAllPartnerProjectsByPartnerId(pageNo, pageSize, sortBy, sortDir, partnerId, search, deleted)
        );
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @PathVariable Long partnerId,
            @RequestParam("prefix") String prefix,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="false") boolean deleted
    ) {
        return ResponseEntity.ok(partnerProjectService.suggestProjects(prefix, size, deleted, partnerId));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<PartnerProjectResponseDto>> getProjectsBatch(
            @PathVariable Long partnerId,
            @RequestParam("ids") List<Long> ids
    ) {
        return ResponseEntity.ok(partnerProjectService.getProjectsByIds(partnerId, ids));
    }

    @GetMapping("/batch/ids")
    public ResponseEntity<List<PartnerProjectResponseDto>> getProjectsBatchByIds(
            @PathVariable("partnerId") Long partnerId,
            @RequestParam("ids") List<Long> ids
    ) {
        return ResponseEntity.ok(partnerProjectService.getProjectsBatchByIds(ids));
    }
}
