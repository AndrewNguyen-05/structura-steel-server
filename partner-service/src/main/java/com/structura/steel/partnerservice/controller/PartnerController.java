package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.service.PartnerService;
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
    public ResponseEntity<ObjectResponse<PartnerResponseDto>> getAllPartners(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(partnerService.getAllPartners(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartnerById(id));
    }

    @PostMapping
    public ResponseEntity<PartnerResponseDto> createPartner(@RequestBody PartnerRequestDto partnerRequestDto) {
        return ResponseEntity.ok(partnerService.createPartner(partnerRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> updatePartner(@PathVariable Long id,
                                                            @RequestBody PartnerRequestDto partnerRequestDto) {
        return ResponseEntity.ok(partnerService.updatePartner(id, partnerRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartnerById(id);
        return ResponseEntity.noContent().build();
    }
}
