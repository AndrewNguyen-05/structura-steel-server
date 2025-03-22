package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.partnerservice.dto.request.PartnerRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    @Override
    public PartnerResponseDto createPartner(PartnerRequestDto dto) {
        Partner partner = partnerMapper.toPartner(dto);
        Partner saved = partnerRepository.save(partner);
        return partnerMapper.toPartnerResponseDto(saved);
    }

    @Override
    public PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto) {
        Partner existing = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));

        // Update fields
        partnerMapper.updatePartnerFromDto(dto, existing);

        Partner updated = partnerRepository.save(existing);
        return partnerMapper.toPartnerResponseDto(updated);
    }

    @Override
    public PartnerResponseDto getPartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
        return partnerMapper.toPartnerResponseDto(partner);
    }

    @Override
    public void deletePartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
        partnerRepository.delete(partner);
    }

    @Override
    public List<PartnerResponseDto> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        return partnerMapper.toPartnerResponseDtoList(partners);
    }
}
