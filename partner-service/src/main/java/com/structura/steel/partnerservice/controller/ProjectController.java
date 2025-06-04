package com.structura.steel.partnerservice.controller;

import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final PartnerProjectService partnerProjectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<PartnerProjectResponseDto> getProjectById(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.ok(partnerProjectService.getProjectById(projectId));
    }

}
