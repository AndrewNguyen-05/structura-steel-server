package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.PartnerResponseDto;

import java.util.List;

public interface PartnerService {

    PartnerResponseDto createPartner(PartnerRequestDto dto);

    PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto);

    PartnerResponseDto getPartnerById(Long id);

    void deletePartnerById(Long id);

    PagingResponse<PartnerResponseDto> getAllPartners(int pageNo, int pageSize, String sortBy, String sortDir, String searchKeyword);

    List<PartnerResponseDto> getPartnersByIds(List<Long> ids);
}
