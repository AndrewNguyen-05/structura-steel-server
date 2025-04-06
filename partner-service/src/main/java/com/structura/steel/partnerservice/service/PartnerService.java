package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.PartnerResponseDto;

public interface PartnerService {

    PartnerResponseDto createPartner(PartnerRequestDto dto);

    PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto);

    PartnerResponseDto getPartnerById(Long id);

    void deletePartnerById(Long id);

    ObjectResponse<PartnerResponseDto> getAllPartners(int pageNo, int pageSize, String sortBy, String sortDir);
}
