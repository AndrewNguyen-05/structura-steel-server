package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
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
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
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
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Warehouse existing = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));
        if (!existing.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Warehouse", "id", warehouseId, "partner", "id", partnerId);
        }
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
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));
        if (!warehouse.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Warehouse", "id", warehouseId, "partner", "id", partnerId);
        }
        return warehouseMapper.toWarehouseResponseDto(warehouse);
    }

    @Override
    public void deleteWarehouse(Long partnerId, Long warehouseId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));
        if (!warehouse.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Warehouse", "id", warehouseId, "partner", "id", partnerId);
        }
        warehouseSearchRepository.deleteById(warehouse.getId()); // luu vao Elastic Search

        warehouseRepository.delete(warehouse);
    }

    @Override
    public PagingResponse<WarehouseResponseDto> getAllWarehousesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));

        String effectiveSortBy = sortBy;
        if ("warehouseCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "warehouseCode.keyword";
        } else if ("warehouseName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "warehouseName.keyword";
        }

        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<WarehouseDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = warehouseSearchRepository.searchByKeyword(searchKeyword, pageable);
            } else {
                pages = warehouseSearchRepository.findAll(pageable);
            }
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<WarehouseDocument> warehouses = pages.getContent();

        // Ep kieu sang dto
        List<WarehouseResponseDto> content = warehouses.stream()
                .map(warehouseMapper::toResponseDtoFromDocument).toList();

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
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
    public List<String> suggestWarehouses(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = warehouseSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(WarehouseDocument::getWarehouseName)
                .distinct()
                .toList();
    }
}
