package com.structura.steel.partnerservice.controller;

import com.structura.steel.partnerservice.dto.request.PartnerProjectRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner-projects")
@RequiredArgsConstructor
public class PartnerProjectController {

    private final PartnerProjectService partnerProjectService;

    @GetMapping
    public ResponseEntity<List<PartnerProjectResponseDto>> getAllPartnerProjects() {
        return ResponseEntity.ok(partnerProjectService.getAllPartnerProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerProjectResponseDto> getPartnerProject(@PathVariable Long id) {
        return ResponseEntity.ok(partnerProjectService.getPartnerProject(id));
    }

    @PostMapping
    public ResponseEntity<PartnerProjectResponseDto> createPartnerProject(
            @RequestBody PartnerProjectRequestDto dto) {
        PartnerProjectResponseDto created = partnerProjectService.createPartnerProject(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerProjectResponseDto> updatePartnerProject(@PathVariable Long id,
                                                                          @RequestBody PartnerProjectRequestDto dto) {
        PartnerProjectResponseDto updated = partnerProjectService.updatePartnerProject(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartnerProject(@PathVariable Long id) {
        partnerProjectService.deletePartnerProject(id);
        return ResponseEntity.noContent().build();
    }
}
