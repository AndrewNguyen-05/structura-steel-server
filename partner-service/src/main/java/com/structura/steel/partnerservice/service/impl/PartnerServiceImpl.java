package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.GetAllPartnerResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerSearchRepository;
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
import org.springframework.util.StringUtils;

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
    private final PartnerSearchRepository partnerSearchRepository;

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
        try {
            PartnerDocument partnerDocument = partnerMapper.toDocument(savedPartner);
            if (StringUtils.hasText(savedPartner.getPartnerName())) {
                partnerDocument.setSuggestion(savedPartner.getPartnerName());
            } else {
                partnerDocument.setSuggestion("");
            }
            partnerSearchRepository.save(partnerDocument);
            log.info("Partner with ID {} and code {} saved to Elasticsearch.", savedPartner.getId(), savedPartner.getPartnerCode());
        } catch (Exception e) {
            log.error("Error saving partner with ID {} to Elasticsearch: {}", savedPartner.getId(), e.getMessage(), e);
        }
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
        try {
            PartnerDocument partnerDocument = partnerMapper.toDocument(updated);
            if (StringUtils.hasText(updated.getPartnerName())) {
                partnerDocument.setSuggestion(updated.getPartnerName());
            } else {
                partnerDocument.setSuggestion("");
            }
            partnerSearchRepository.save(partnerDocument);
        } catch (Exception e) {
            log.error("Error saving partner with ID {} to Elasticsearch: {}", updated.getId(), e.getMessage(), e);
        }
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

        partnerSearchRepository.deleteById(partner.getId());
        partnerRepository.delete(partner);
    }

    @Override
    public PagingResponse<GetAllPartnerResponseDto> getAllPartners(int pageNo, int pageSize, String sortBy, String sortDir, String searchKeyword) {

        String effectiveSortBy = sortBy;
        if ("partnerCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "partnerCode.keyword";
        } else if ("partnerName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "partnerName.keyword";
        }
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<PartnerDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = partnerSearchRepository.searchByKeyword(searchKeyword, pageable);
            } else {
                pages = partnerSearchRepository.findAll(pageable);
            }
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<PartnerDocument> products = pages.getContent();

        // Ep kieu sang dto
        List<GetAllPartnerResponseDto> content = products.stream().map(partnerMapper::toResponseDtoFromDocument).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<GetAllPartnerResponseDto> response = new PagingResponse<>();
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

    @Override
    @Transactional(readOnly = true)
    public List<String> suggestPartners(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = partnerSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(PartnerDocument::getPartnerName)
                .distinct()
                .toList();
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
