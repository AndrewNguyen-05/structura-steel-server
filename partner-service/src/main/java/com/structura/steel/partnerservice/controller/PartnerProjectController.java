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

    @GetMapping
    public ResponseEntity<PagingResponse<PartnerProjectResponseDto>> getAllPartnerProjects(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long partnerId,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {
        return ResponseEntity.ok(partnerProjectService.getAllPartnerProjectsByPartnerId(pageNo, pageSize, sortBy, sortDir, partnerId, searchKeyword));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam("prefix") String prefix,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(partnerProjectService.suggestProjects(prefix, size));
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
