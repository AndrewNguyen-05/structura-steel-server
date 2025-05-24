package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.commons.dto.partner.request.WarehouseRequestDto;
import com.structura.steel.commons.dto.partner.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.WarehouseDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.WarehouseSearchRepository;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.Warehouse;
import com.structura.steel.partnerservice.mapper.WarehouseMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
import com.structura.steel.partnerservice.service.WarehouseService;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final PartnerRepository partnerRepository;
    private final WarehouseMapper warehouseMapper;
    private final WarehouseSearchRepository warehouseSearchRepository;

    @Override
    public WarehouseResponseDto createWarehouse(Long partnerId, WarehouseRequestDto dto) {
        Partner partner = getValidPartner(partnerId);
        Warehouse warehouse = warehouseMapper.toWarehouse(dto);
        warehouse.setPartner(partner);
        warehouse.setWarehouseCode(CodeGenerator.generateCode(EntityType.PARTNER));
        Warehouse saved = warehouseRepository.save(warehouse);

        WarehouseDocument warehouseDocument = warehouseMapper.toDocument(saved);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(saved.getPartner().getPartnerName())) {
            warehouseDocument.setSuggestion(saved.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            warehouseDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        warehouseSearchRepository.save(warehouseDocument); // luu vao Elastic Search

        return warehouseMapper.toWarehouseResponseDto(saved);
    }

    @Override
    public WarehouseResponseDto updateWarehouse(Long partnerId, Long warehouseId, WarehouseRequestDto dto) {
        getValidPartner(partnerId);

        Warehouse existing = getValidWarehouse(partnerId, warehouseId, false);
        warehouseMapper.updateWarehouseFromDto(dto, existing);
        Warehouse updated = warehouseRepository.save(existing);

        WarehouseDocument warehouseDocument = warehouseMapper.toDocument(updated);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(updated.getPartner().getPartnerName())) {
            warehouseDocument.setSuggestion(updated.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            warehouseDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        warehouseSearchRepository.save(warehouseDocument); // luu vao Elastic Search

        return warehouseMapper.toWarehouseResponseDto(updated);
    }

    @Override
    public WarehouseResponseDto getWarehouse(Long partnerId, Long warehouseId) {
        getValidPartner(partnerId);

        Warehouse warehouse = getValidWarehouse(partnerId, warehouseId, false);
        return warehouseMapper.toWarehouseResponseDto(warehouse);
    }

    @Override
    public void deleteWarehouse(Long partnerId, Long warehouseId) {
        getValidPartner(partnerId);

        Warehouse warehouse = getValidWarehouse(partnerId, warehouseId, true);

        warehouseSearchRepository.deleteById(warehouse.getId()); // luu vao Elastic Search

        warehouseRepository.delete(warehouse);
    }

    @Override
    public void softDeleteWarehouse(Long partnerId, Long warehouseId) {
        getValidPartner(partnerId);
        Warehouse warehouse = getValidWarehouse(partnerId, warehouseId, false); // Get non-deleted

        warehouse.setDeleted(true);
        warehouseRepository.save(warehouse);
        saveToElasticsearch(warehouse);
        log.info("Warehouse with ID {} soft-deleted.", warehouseId);
    }

    @Override
    public WarehouseResponseDto restoreWarehouse(Long partnerId, Long warehouseId) {
        getValidPartner(partnerId);
        Warehouse warehouse = getValidWarehouse(partnerId, warehouseId, true); // Get deleted

        warehouse.setDeleted(false);
        Warehouse restored = warehouseRepository.save(warehouse);
        saveToElasticsearch(restored);
        log.info("Warehouse with ID {} restored.", warehouseId);

        return warehouseMapper.toWarehouseResponseDto(restored);
    }

    @Override
    public PagingResponse<WarehouseResponseDto> getAllWarehousesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword, boolean deleted) {
        getValidPartner(partnerId);

        String effectiveSortBy = sortBy;
        if ("warehouseCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "warehouseCode.keyword";
        } else if ("warehouseName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "warehouseName.keyword";
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<WarehouseDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = warehouseSearchRepository.searchByKeywordAndPartnerId(searchKeyword, deleted, partnerId, pageable);
            } else {
                pages = warehouseSearchRepository.getAllByPartnerIdAndDeleted(partnerId, deleted, pageable);
            }
        } catch (Exception ex) {
            log.error("Elasticsearch query failed for warehouses (Partner ID: {}): {}", partnerId, ex.getMessage(), ex);
            pages = Page.empty(pageable);
        }

        List<WarehouseResponseDto> content = pages.getContent().stream()
                .map(warehouseMapper::toResponseDtoFromDocument)
                .collect(Collectors.toList());

        PagingResponse<WarehouseResponseDto> response = new PagingResponse<>();
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
    public List<String> suggestWarehouses(String prefix, int size, boolean deleted, Long partnerId) {
        getValidPartner(partnerId);
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(0, size);
        Page<WarehouseDocument> page = warehouseSearchRepository.findBySuggestionPrefix(prefix, deleted, partnerId, pageable);
        return page.getContent().stream()
                .map(WarehouseDocument::getWarehouseName)
                .distinct()
                .collect(Collectors.toList());
    }

    private Partner getValidPartner(Long partnerId) {
        return partnerRepository.findByIdAndDeletedFalse(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId + " (or it might be deleted)"));
    }

    private Warehouse getValidWarehouse(Long partnerId, Long warehouseId, boolean expectDeleted) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId) // Fetch any first
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));

        if (!warehouse.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Warehouse", "id", warehouseId, "partner", "id", partnerId);
        }

        if (warehouse.getDeleted() != expectDeleted) {
            throw new ResourceNotFoundException("Warehouse", "id", warehouseId +
                    (expectDeleted ? " (is not deleted)" : " (is deleted)"));
        }
        return warehouse;
    }

    private void saveToElasticsearch(Warehouse warehouse) {
        try {
            WarehouseDocument doc = warehouseMapper.toDocument(warehouse);
            String suggestion = (warehouse.getWarehouseCode() != null ? warehouse.getWarehouseCode() : "") + " "
                    + (warehouse.getWarehouseName() != null ? warehouse.getWarehouseName() : "");
            doc.setSuggestion(suggestion.trim());
            doc.setDeleted(warehouse.getDeleted()); // Ensure deleted status is set
            warehouseSearchRepository.save(doc);
            log.info("Warehouse with ID {} saved to Elasticsearch.", warehouse.getId());
        } catch (Exception e) {
            log.error("Error saving warehouse with ID {} to Elasticsearch: {}", warehouse.getId(), e.getMessage(), e);
        }
    }
}
