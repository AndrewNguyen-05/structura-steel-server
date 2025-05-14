package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    private final ProductFeignClient productFeignClient;

    private final PartnerProjectMapper partnerProjectMapper;
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
        partner.setPartnerCode(CodeGenerator.generateCode(EntityType.PARTNER));

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
        return toPartnerResponseDtoWithProductInProject(updated);
    }

    @Override
    public PartnerResponseDto getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));
        PartnerResponseDto result;
        try {
            result = toPartnerResponseDtoWithProductInProject(partner);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
        return result;
    }

    @Override
    public void deletePartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));

        partnerRepository.delete(partner);
    }

    @Override
    public PagingResponse<PartnerResponseDto> getAllPartners(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<Partner> pages = partnerRepository.findAll(pageable);

        // Lay ra gia tri (content) cua page
        List<Partner> products = pages.getContent();


        // Ep kieu sang dto
        List<PartnerResponseDto> content = products.stream().map(partnerMapper::toPartnerResponseDto).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<PartnerResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }

    @Override
    public List<PartnerResponseDto> getPartnersByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<Partner> partners = partnerRepository.findAllById(ids);
        return partners.stream()
                .map(partnerMapper::toPartnerResponseDto)
                .collect(Collectors.toList());
    }

    private PartnerResponseDto toPartnerResponseDtoWithProductInProject(Partner partner) {
        PartnerResponseDto responseDto = partnerMapper.toPartnerResponseDto(partner);

        if (partner.getPartnerProjects() != null && !partner.getPartnerProjects().isEmpty()) {
            for(int i = 0; i < partner.getPartnerProjects().size(); i++) {
                List<Long> productIds = partner.getPartnerProjects().get(i).getProductIds();
                List<ProductResponseDto> products = new ArrayList<>();
                if (productIds != null && !productIds.isEmpty()) {
                    for (Long productId : productIds) {
                        ProductResponseDto product = productFeignClient.getProductById(productId);
                        products.add(product);
                    }
                }
                responseDto.partnerProjects().get(i).setProducts(products);
            }
        }

        return responseDto;
    }
}
