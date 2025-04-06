package com.structura.steel.partnerservice.controller;

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
    public ResponseEntity<List<PartnerResponseDto>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
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
