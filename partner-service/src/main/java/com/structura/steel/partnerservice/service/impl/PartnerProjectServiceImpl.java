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
                .map(partnerProjectMapper::toResponseDtoFromDocument)
                .collect(Collectors.toList());

        // Fetch product details
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

        List<ProductResponseDto> products = fetchFilterAndUpdateProducts(project);

        responseDto.setProducts(products);
        return responseDto;
    }

    private List<ProductResponseDto> fetchFilterAndUpdateProducts(PartnerProject project) {
        List<Long> currentProductIds = project.getProductIds();
        List<Long> idsToRemove = new ArrayList<>();
        List<ProductResponseDto> validProducts = new ArrayList<>();

        // Trả về list rỗng nếu không có ID
        if (currentProductIds == null || currentProductIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Tạo một bản sao để lặp
        List<Long> idsToFetch = new ArrayList<>(currentProductIds);

        for (Long productId : idsToFetch) {
            try {
                ProductResponseDto product = productFeignClient.getProductById(productId);

                // Kiểm tra Soft Delete - Giả sử DTO của bạn có phương thức deleted()
                // Nếu là getter thì dùng product.getDeleted()
                if (product.deleted()) {
                    log.warn("Product ID {} is soft-deleted, marking for removal from project {}.", productId, project.getId());
                    idsToRemove.add(productId);
                } else {
                    validProducts.add(product);
                }
            } catch (StructuraSteelException e) {
                // Xử lý Hard Delete (404)
                if(e.getStatus().equals(HttpStatus.NOT_FOUND)) {
                    log.warn("Product ID {} not found (404), marking for removal from project {}.", productId, project.getId());
                    idsToRemove.add(productId);
                } else {
                    // Xử lý các lỗi khác (mạng, server...)
                    log.error("Failed to fetch product ID {} for project {}. Skipping. Error: {}", productId, project.getId(), e.getMessage());
                    // Không xóa ID trong trường hợp lỗi tạm thời, chỉ bỏ qua lần này.
                }

            }
        }

        // Nếu có ID cần xóa
        if (!idsToRemove.isEmpty()) {
            log.info("Project {} has IDs to remove: {}. Attempting DB update.", project.getId(), idsToRemove);

            // **QUAN TRỌNG**: Lấy lại entity từ DB để đảm bảo nó được quản lý (managed)
            PartnerProject projectToUpdate = partnerProjectRepository.findById(project.getId()).orElse(null);

            if (projectToUpdate != null && projectToUpdate.getProductIds() != null) {

                // **SỬA LỖI CHÍNH**: Tạo một list MỚI thay vì sửa list cũ
                List<Long> newProductIds = new ArrayList<>(projectToUpdate.getProductIds());

                // Thực hiện xóa trên list mới
                boolean changed = newProductIds.removeAll(idsToRemove);

                if (changed) {
                    log.info("Project {} - List changed. Old size: {}, New size: {}. Saving...",
                            project.getId(), projectToUpdate.getProductIds().size(), newProductIds.size());

                    // Gán list MỚI vào entity
                    projectToUpdate.setProductIds(newProductIds);

                    try {
                        // Sử dụng saveAndFlush để ép ghi xuống DB ngay lập tức và bắt lỗi sớm
                        PartnerProject updated = partnerProjectRepository.saveAndFlush(projectToUpdate);

                        // Cập nhật Elasticsearch SAU KHI DB thành công
                        saveToElasticsearch(updated);
                        log.info("Successfully removed IDs {} from project {}. DB & ES updated.", idsToRemove, project.getId());

                        // Cập nhật lại đối tượng 'project' gốc để caller có dữ liệu mới nhất
                        project.setProductIds(updated.getProductIds());

                    } catch (Exception e) {
                        log.error("Failed to save/update project {} after removing product IDs: {}", project.getId(), e.getMessage(), e);
                    }
                } else {
                    // Ghi log nếu vì lý do nào đó removeAll không thay đổi list
                    log.warn("Project {} - IDs {} were marked for removal, but list did not change. Current IDs: {}",
                            project.getId(), idsToRemove, projectToUpdate.getProductIds());
                }
            } else {
                log.error("Could not find project {} in DB or it has no product IDs to update.", project.getId());
            }
        }

        return validProducts; // Trả về danh sách sản phẩm hợp lệ
    }
}
