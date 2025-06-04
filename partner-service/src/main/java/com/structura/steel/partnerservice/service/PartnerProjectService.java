package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.partner.request.PartnerProjectRequestDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;

import java.util.List;

public interface PartnerProjectService {

    PartnerProjectResponseDto createPartnerProject(Long partnerId, PartnerProjectRequestDto dto);

    PartnerProjectResponseDto updatePartnerProject(Long partnerId, Long projectId, PartnerProjectRequestDto dto);

    PartnerProjectResponseDto getPartnerProject(Long partnerId, Long projectId);

    PartnerProjectResponseDto getProjectById(Long projectId);

    void deletePartnerProject(Long partnerId, Long projectId);

    PagingResponse<PartnerProjectResponseDto> getAllPartnerProjectsByPartnerId(
            int pageNo, int pageSize, String sortBy, String sortDir,
            Long partnerId, String search, boolean deleted);

    List<String> suggestProjects(String prefix, int size, boolean deleted, Long partnerId);

    List<PartnerProjectResponseDto> getProjectsByIds(Long partnerId, List<Long> ids);

    List<PartnerProjectResponseDto> getProjectsBatchByIds(List<Long> ids);

    void softDeletePartnerProject(Long partnerId, Long projectId);

    PartnerProjectResponseDto restorePartnerProject(Long partnerId, Long projectId);
}
