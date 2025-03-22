package com.structura.steel.partnerservice.controller;

import com.structura.steel.partnerservice.dto.request.PartnerRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerResponseDto>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartner(id));
    }

    @PostMapping
    public ResponseEntity<PartnerResponseDto> createPartner(@RequestBody PartnerRequestDto dto) {
        PartnerResponseDto created = partnerService.createPartner(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponseDto> updatePartner(@PathVariable Long id,
                                                            @RequestBody PartnerRequestDto dto) {
        PartnerResponseDto updated = partnerService.updatePartner(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}
