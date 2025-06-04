package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.exception.StructuraSteelException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.commons.dto.partner.request.PartnerProjectRequestDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerProjectSearchRepository;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
        Partner partner = getValidPartner(partnerId);

        PartnerProject project = partnerProjectMapper.toPartnerProject(dto);
        project.setPartner(partner);
        project.setProjectCode(CodeGenerator.generateCode(EntityType.PROJECT));
        PartnerProject saved = partnerProjectRepository.save(project);

        PartnerProjectDocument partnerProjectDocument = partnerProjectMapper.toDocument(saved);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(saved.getPartner().getPartnerName())) {
            partnerProjectDocument.setSuggestion(saved.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            partnerProjectDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        partnerProjectSearchRepository.save(partnerProjectDocument); // luu vao Elastic Search

        return entityToResponseWithProduct(saved);
    }

    @Override
    public PartnerProjectResponseDto updatePartnerProject(Long partnerId, Long projectId, PartnerProjectRequestDto dto) {
        getValidPartner(partnerId);
        PartnerProject existing = getValidProject(partnerId, projectId, false);

        partnerProjectMapper.updatePartnerProjectFromDto(dto, existing);
        PartnerProject updated = partnerProjectRepository.save(existing);

        PartnerProjectDocument partnerProjectDocument = partnerProjectMapper.toDocument(updated);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(updated.getPartner().getPartnerName())) {
            partnerProjectDocument.setSuggestion(updated.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            partnerProjectDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        partnerProjectSearchRepository.save(partnerProjectDocument); // luu vao Elastic Search

        return entityToResponseWithProduct(updated);
    }

    @Override
    public PartnerProjectResponseDto getPartnerProject(Long partnerId, Long projectId) {
        getValidPartner(partnerId);
        PartnerProject project = getValidProject(partnerId, projectId, false);
        return entityToResponseWithProduct(project);
    }

    @Override
    public PartnerProjectResponseDto getProjectById(Long projectId) {
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        if (project.getDeleted()) {
            throw new ResourceNotFoundException("Project", "id", projectId + " (is deleted)");
        }
        return entityToResponseWithProduct(project);
    }

    @Override
    public void deletePartnerProject(Long partnerId, Long projectId) {
        getValidPartner(partnerId);
        PartnerProject project = getValidProject(partnerId, projectId, true);

        partnerProjectSearchRepository.deleteById(project.getId());

        partnerProjectRepository.delete(project);
    }

    @Override
    public void softDeletePartnerProject(Long partnerId, Long projectId) {
        getValidPartner(partnerId);
        PartnerProject project = getValidProject(partnerId, projectId, false);

        project.setDeleted(true);
        partnerProjectRepository.save(project);
        saveToElasticsearch(project);
        log.info("Project with ID {} soft-deleted.", projectId);
    }

    @Override
    public PartnerProjectResponseDto restorePartnerProject(Long partnerId, Long projectId) {
        getValidPartner(partnerId);
        PartnerProject project = getValidProject(partnerId, projectId, true);

        project.setDeleted(false);
        PartnerProject restored = partnerProjectRepository.save(project);
        saveToElasticsearch(restored);
        log.info("Project with ID {} restored.", projectId);

        return entityToResponseWithProduct(restored);
    }

    @Override
    public PagingResponse<PartnerProjectResponseDto> getAllPartnerProjectsByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword, boolean deleted) {
        getValidPartner(partnerId);

        String effectiveSortBy = sortBy;
        if ("projectCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "projectCode.keyword";
        } else if ("projectName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "projectName.keyword";
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PartnerProjectDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = partnerProjectSearchRepository.searchByKeywordAndPartnerId(searchKeyword, partnerId, deleted, pageable);
            } else {
                pages = partnerProjectSearchRepository.getAllByPartnerIdAndDeleted(partnerId, deleted, pageable);
            }
        } catch (Exception ex) {
            log.error("Elasticsearch query failed for projects (Partner ID: {}): {}", partnerId, ex.getMessage(), ex);
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<PartnerProjectDocument> projects = pages.getContent();

        // Ep kieu
        List<PartnerProjectResponseDto> content = projects.stream()
                .map(this::documentToResponseWithProduct)
                .collect(Collectors.toList());

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
    public List<String> suggestProjects(String prefix, int size, boolean deleted, Long partnerId) {
        getValidPartner(partnerId);
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(0, size);
        Page<PartnerProjectDocument> page = partnerProjectSearchRepository.findBySuggestionPrefix(prefix, deleted, partnerId, pageable);
        return page.getContent().stream()
                .map(PartnerProjectDocument::getProjectName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<PartnerProjectResponseDto> getProjectsByIds(Long partnerId, List<Long> ids) {
        getValidPartner(partnerId);

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

    private Partner getValidPartner(Long partnerId) {
        return partnerRepository.findByIdAndDeletedFalse(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId + " (or it might be deleted)"));
    }

    private PartnerProject getValidProject(Long partnerId, Long projectId, boolean expectDeleted) {
        PartnerProject project = partnerProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        if (!project.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Project", "id", projectId, "partner", "id", partnerId);
        }

        if (project.getDeleted() != expectDeleted) {
            throw new ResourceNotFoundException("Project", "id", projectId +
                    (expectDeleted ? " (is not deleted)" : " (is deleted)"));
        }

        return project;
    }

    private void saveToElasticsearch(PartnerProject project) {
        try {
            PartnerProjectDocument doc = partnerProjectMapper.toDocument(project);
            String suggestion = (project.getProjectCode() != null ? project.getProjectCode() : "") + " "
                    + (project.getProjectName() != null ? project.getProjectName() : "");
            doc.setSuggestion(suggestion.trim());
            doc.setDeleted(project.getDeleted()); // Ensure deleted is set
            partnerProjectSearchRepository.save(doc);
            log.info("Project with ID {} saved to Elasticsearch.", project.getId());
        } catch (Exception e) {
            log.error("Error saving project with ID {} to Elasticsearch: {}", project.getId(), e.getMessage(), e);
        }
    }

    private PartnerProjectResponseDto entityToResponseWithProduct(PartnerProject project) {
        PartnerProjectResponseDto responseDto = partnerProjectMapper.toPartnerProjectResponseDto(project);

        List<ProductResponseDto> products = fetchProductsAndHandleUpdates(project.getId(), project.getProductIds());

        responseDto.setProducts(products);
        return responseDto;
    }

    private PartnerProjectResponseDto documentToResponseWithProduct(PartnerProjectDocument document) {
        PartnerProjectResponseDto responseDto = partnerProjectMapper.toResponseDtoFromDocument(document);

        List<ProductResponseDto> products = fetchProductsAndHandleUpdates(document.getId(), document.getProductIds());

        responseDto.setProducts(products);
        return responseDto;
    }

    private List<ProductResponseDto> fetchProductsAndHandleUpdates(Long projectId, List<Long> currentProductIds) {
        List<Long> idsToRemove = new ArrayList<>();
        List<ProductResponseDto> validProducts = new ArrayList<>();
        List<Long> foundIds = new ArrayList<>();

        if (currentProductIds == null || currentProductIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // *** GỌI BATCH ĐỂ LẤY NHIỀU SẢN PHẨM CÙNG LÚC ***
            log.debug("Fetching batch products for project {}: {}", projectId, currentProductIds);
            List<ProductResponseDto> returnedProducts = productFeignClient.getProductsBatch(new ArrayList<>(currentProductIds));
            log.debug("Received {} products from batch call for project {}.", returnedProducts.size(), projectId);

            // Xử lý kết quả trả về
            for (ProductResponseDto product : returnedProducts) {
                foundIds.add(product.id()); // Giả sử DTO có .id()

                if (product.deleted()) { // Giả sử DTO có .deleted()
                    log.warn("Product ID {} is soft-deleted, skipping for project {}.", product.id(), projectId);
                    // Nếu muốn xóa cả SP soft-deleted khỏi project, thêm dòng sau:
                    // idsToRemove.add(product.id());
                } else {
                    validProducts.add(product);
                }
            }

            // Xác định các ID bị Hard-Delete (yêu cầu nhưng không tìm thấy)
            List<Long> hardDeletedIds = new ArrayList<>(currentProductIds);
            hardDeletedIds.removeAll(foundIds);
            idsToRemove.addAll(hardDeletedIds); // Thêm các ID 404 vào danh sách cần xóa

            if (!hardDeletedIds.isEmpty()) {
                log.warn("Product IDs {} not found (404/hard-deleted), marking for removal from project {}.", hardDeletedIds, projectId);
            }

        } catch (Exception e) {
            // Xử lý nếu gọi batch bị lỗi hoàn toàn (vd: ProductService sập)
            log.error("Failed to fetch batch products for project {}. Error: {}. Cannot determine 404s.", projectId, e.getMessage(), e);
            // Trong trường hợp này, ta không thể biết cái nào 404, nên chỉ trả về rỗng và không update.
            // Hoặc bạn có thể implement fallback gọi 1-1 ở đây nếu muốn.
            return Collections.emptyList();
        }

        // Nếu có ID cần xóa (chỉ hard-deleted theo logic hiện tại)
        if (!idsToRemove.isEmpty()) {
            // Gọi hàm cập nhật riêng
            updateProjectProductList(projectId, idsToRemove);
        }

        return validProducts;
    }

    private void updateProjectProductList(Long projectId, List<Long> idsToRemove) {
        log.info("Project {} has IDs to remove: {}. Attempting DB update.", projectId, idsToRemove);
        PartnerProject projectToUpdate = partnerProjectRepository.findById(projectId).orElse(null);

        if (projectToUpdate != null && projectToUpdate.getProductIds() != null) {
            List<Long> newProductIds = new ArrayList<>(projectToUpdate.getProductIds());
            boolean changed = newProductIds.removeAll(idsToRemove);

            if (changed) {
                log.info("Project {} - List changed. Saving...", projectId);
                projectToUpdate.setProductIds(newProductIds);
                try {
                    PartnerProject updated = partnerProjectRepository.saveAndFlush(projectToUpdate);
                    saveToElasticsearch(updated);
                    log.info("Successfully removed IDs {} from project {}.", idsToRemove, projectId);
                } catch (Exception e) {
                    log.error("Failed to save/update project {} after removing IDs: {}", projectId, e.getMessage(), e);
                }
            } else {
                log.warn("Project {} - IDs {} marked, but list didn't change.", projectId, idsToRemove);
            }
        } else {
            log.error("Could not find project {} in DB to update.", projectId);
        }
    }
}
