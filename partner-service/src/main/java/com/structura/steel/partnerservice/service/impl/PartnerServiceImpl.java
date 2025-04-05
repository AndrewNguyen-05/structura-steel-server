package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    @Override
    public PartnerResponseDto createPartner(PartnerRequestDto dto) {
        // Chỉ xử lý các thông tin của Partner
        Partner partner = new Partner();
        partner.setPartnerType(dto.partnerType());
        partner.setPartnerName(dto.partnerName());
        partner.setTaxCode(dto.taxCode());
        partner.setLegalRepresentative(dto.legalRepresentative());
        partner.setLegalRepresentativePhone(dto.legalRepresentativePhone());
        partner.setContactPerson(dto.contactPerson());
        partner.setContactPersonPhone(dto.contactPersonPhone());
        partner.setBankName(dto.bankName());
        partner.setBankAccountNumber(dto.bankAccountNumber());

        // Nếu DTO chứa dữ liệu của các entity con, bạn có thể bỏ qua hoặc xử lý theo yêu cầu riêng.
        Partner savedPartner = partnerRepository.save(partner);
        return partnerMapper.toPartnerResponseDto(savedPartner);
    }

    @Override
    public PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto) {
        Partner existing = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));

        // Chỉ cập nhật các trường của Partner (không cập nhật các entity con)
        existing.setPartnerType(dto.partnerType());
        existing.setPartnerName(dto.partnerName());
        existing.setTaxCode(dto.taxCode());
        existing.setLegalRepresentative(dto.legalRepresentative());
        existing.setLegalRepresentativePhone(dto.legalRepresentativePhone());
        existing.setContactPerson(dto.contactPerson());
        existing.setContactPersonPhone(dto.contactPersonPhone());
        existing.setBankName(dto.bankName());
        existing.setBankAccountNumber(dto.bankAccountNumber());

        Partner updated = partnerRepository.save(existing);
        return partnerMapper.toPartnerResponseDto(updated);
    }

    @Override
    public PartnerResponseDto getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));
        return partnerMapper.toPartnerResponseDto(partner);
    }

    @Override
    public void deletePartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));
        // Nếu Cascade chưa được cấu hình, bạn có thể xoá các entity con trước.
        // Nhưng theo Phương án 3, các thao tác trên các entity con được thực hiện riêng qua endpoint khác.
        partnerRepository.delete(partner);
    }

    @Override
    public List<PartnerResponseDto> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        return partners.stream().map(partnerMapper::toPartnerResponseDto).collect(Collectors.toList());
    }
}
