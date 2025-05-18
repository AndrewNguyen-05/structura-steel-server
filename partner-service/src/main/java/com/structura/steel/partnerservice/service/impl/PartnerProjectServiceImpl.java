package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.PartnerProjectRequestDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerProjectSearchRepository;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartnerProjectServiceImpl implements PartnerProjectService {

    private final PartnerProjectRepository partnerProjectRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerProjectMapper partnerProjectMapper;

    private final ProductFeignClient productFeignClient;
    private final PartnerProjectSearchRepository partnerProjectSearchRepository;

    @Override
    public PartnerProjectResponseDto createPartnerProject(Long partnerId, PartnerProjectRequestDto dto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));

        PartnerProject project = partnerProjectMapper.toPartnerProject(dto);
        project.setPartner(partner);
        project.setProjectCode(CodeGenerator.generateCode(EntityType.PROJECT));
        PartnerProject saved = partnerProjectRepository.save(project);

        return entityToResponseWithProduct(saved);
    }

    @Override
    public PartnerProjectResponseDto updatePartnerProject(Long partnerId, Long projectId, PartnerProjectRequestDto dto) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        PartnerProject existing = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner's project", "id", projectId));

        // Kiểm tra xem project có thuộc partnerId không (nếu cần)
        if (!existing.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Partner's project", "id", projectId, "partner", "id", partnerId);
        }

        partnerProjectMapper.updatePartnerProjectFromDto(dto, existing);
        PartnerProject updated = partnerProjectRepository.save(existing);
        return entityToResponseWithProduct(updated);
    }

    @Override
    public PartnerProjectResponseDto getPartnerProject(Long partnerId, Long projectId) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner's project", "id", projectId));
        if (!project.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Partner's project", "id", projectId, "partner", "id", partnerId);
        }
        return entityToResponseWithProduct(project);
    }

    @Override
    public void deletePartnerProject(Long partnerId, Long projectId) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner's project", "id", projectId));
        if (!project.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Partner's project", "id", projectId, "partner", "id", partnerId);
        }
        partnerProjectRepository.delete(project);
    }

    @Override
    public PagingResponse<PartnerProjectResponseDto> getAllPartnerProjectsByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));

        String effectiveSortBy = sortBy;
        if ("projectCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "projectCode.keyword";
        } else if ("projectName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "projectName.keyword";
        }

        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<PartnerProjectDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = partnerProjectSearchRepository.searchByKeyword(searchKeyword, pageable);
            } else {
                pages = partnerProjectSearchRepository.findAll(pageable);
            }
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<PartnerProjectDocument> projects = pages.getContent();

        // Ep kieu sang dto
        List<PartnerProjectResponseDto> content = projects.stream()
                .map(partnerProjectMapper::toResponseDtoFromDocument).toList();

        for(int i = 0; i < projects.size(); i++) {
            List<Long> productIds = projects.get(i).getProductIds();
            List<ProductResponseDto> products = new ArrayList<>();
            if (productIds != null) {
                for (Long productId : productIds) {
                    ProductResponseDto product = productFeignClient.getProductById(productId);
                    products.add(product);
                }
            }
            content.get(i).setProducts(products);
        }

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<PartnerProjectResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> suggestProjects(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = partnerProjectSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(PartnerProjectDocument::getProjectName)
                .distinct()
                .toList();
    }

    @Override
    public List<PartnerProjectResponseDto> getProjectsByIds(Long partnerId, List<Long> ids) {
        partnerProjectRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));

        List<PartnerProject> projects = partnerProjectRepository.findAllByIdInAndPartnerId(ids, partnerId);

        return projects.stream()
                .map(this::entityToResponseWithProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartnerProjectResponseDto> getProjectsBatchByIds(List<Long> ids) {

        List<PartnerProject> projects = partnerProjectRepository.findAllById(ids);

        return projects.stream()
                .map(this::entityToResponseWithProduct)
                .collect(Collectors.toList());
    }

    private PartnerProjectResponseDto entityToResponseWithProduct(PartnerProject project) {
        PartnerProjectResponseDto responseDto = partnerProjectMapper.toPartnerProjectResponseDto(project);

        List<Long> productIds = project.getProductIds();
        if (productIds != null && !productIds.isEmpty()) {
            List<ProductResponseDto> products = productFeignClient.getProductsBatch(productIds);
            responseDto.setProducts(products);
        } else {
            responseDto.setProducts(Collections.emptyList());
        }

        return responseDto;
    }
}
