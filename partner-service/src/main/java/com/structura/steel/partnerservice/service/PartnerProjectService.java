package com.structura.steel.partnerservice.service;

import com.structura.steel.partnerservice.dto.request.PartnerProjectRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerProjectResponseDto;

import java.util.List;

public interface PartnerProjectService {
    PartnerProjectResponseDto createPartnerProject(PartnerProjectRequestDto dto);
    PartnerProjectResponseDto updatePartnerProject(Long id, PartnerProjectRequestDto dto);
    PartnerProjectResponseDto getPartnerProject(Long id);
    void deletePartnerProject(Long id);
    List<PartnerProjectResponseDto> getAllPartnerProjects();
}
