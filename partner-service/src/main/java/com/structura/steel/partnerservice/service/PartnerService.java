package com.structura.steel.partnerservice.service;

import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.partner.request.PartnerRequestDto;
import com.structura.steel.commons.dto.partner.response.GetAllPartnerResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;

import java.util.List;

public interface PartnerService {

    PartnerResponseDto createPartner(PartnerRequestDto dto);

    PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto);

    PartnerResponseDto getPartnerById(Long id);

    void deletePartnerById(Long id);

    PagingResponse<GetAllPartnerResponseDto> getAllPartners(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted, String searchKeyword);

    List<PartnerResponseDto> getPartnersByIds(List<Long> ids);

    List<String> suggestPartners(String prefix, boolean deleted, int size);

    void softDeletePartnerById(Long id);

    PartnerResponseDto restorePartnerById(Long id);

    void updatePartnerDebt(Long partnerId, UpdatePartnerDebtRequestDto dto);
}
