package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.dto.partner.request.PartnerRequestDto;
import com.structura.steel.commons.dto.partner.response.GetAllPartnerResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.partnerservice.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<PagingResponse<GetAllPartnerResponseDto>> getAllPartners(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value= "deleted", defaultValue = AppConstants.DELETED) boolean deleted,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {
        return ResponseEntity.ok(partnerService.getAllPartners(pageNo, pageSize, sortBy, sortDir, deleted, searchKeyword));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam("prefix") String prefix,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value= "deleted", defaultValue = AppConstants.DELETED) boolean deleted
    ) {
        return ResponseEntity.ok(partnerService.suggestPartners(prefix, deleted, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartnerById(id));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<PartnerResponseDto>> getPartnersByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(partnerService.getPartnersByIds(ids));
    }


    @PostMapping
    public ResponseEntity<PartnerResponseDto> createPartner(@RequestBody @Valid PartnerRequestDto partnerRequestDto) {
        return ResponseEntity.ok(partnerService.createPartner(partnerRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> updatePartner(@PathVariable Long id,
                                                            @RequestBody @Valid PartnerRequestDto partnerRequestDto) {
        return ResponseEntity.ok(partnerService.updatePartner(id, partnerRequestDto));
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeletePartner(@PathVariable Long id) {
        partnerService.softDeletePartnerById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<PartnerResponseDto> restorePartner(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.restorePartnerById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartnerById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/update-debt")
    public ResponseEntity<Void> updatePartnerDebt(
            @PathVariable Long id,
            @RequestBody UpdatePartnerDebtRequestDto dto) {
        partnerService.updatePartnerDebt(id, dto);
        return ResponseEntity.ok().build();
    }
}
