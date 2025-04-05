package com.structura.steel.partnerservice.service;

import com.structura.steel.partnerservice.dto.request.PartnerRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerResponseDto;
import java.util.List;

public interface PartnerService {
    PartnerResponseDto createPartner(PartnerRequestDto dto);
    PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto);
    PartnerResponseDto getPartnerById(Long id);
    void deletePartnerById(Long id);
    List<PartnerResponseDto> getAllPartners();
}
