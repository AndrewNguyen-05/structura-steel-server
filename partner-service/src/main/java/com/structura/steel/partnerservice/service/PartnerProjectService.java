package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.dto.request.PartnerProjectRequestDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;

public interface PartnerProjectService {

    PartnerProjectResponseDto createPartnerProject(Long partnerId, PartnerProjectRequestDto dto);

    PartnerProjectResponseDto updatePartnerProject(Long partnerId, Long projectId, PartnerProjectRequestDto dto);

    PartnerProjectResponseDto getPartnerProject(Long partnerId, Long projectId);

    void deletePartnerProject(Long partnerId, Long projectId);

    ObjectResponse<PartnerProjectResponseDto> getAllPartnerProjectsByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId);
}
