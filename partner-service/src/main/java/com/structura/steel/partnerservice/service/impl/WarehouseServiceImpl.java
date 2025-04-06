package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.Warehouse;
import com.structura.steel.partnerservice.mapper.WarehouseMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
import com.structura.steel.partnerservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final PartnerRepository partnerRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponseDto createWarehouse(Long partnerId, WarehouseRequestDto dto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Warehouse warehouse = warehouseMapper.toWarehouse(dto);
        warehouse.setPartner(partner);
        Warehouse saved = warehouseRepository.save(warehouse);
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
        warehouseRepository.delete(warehouse);
    }

    @Override
    public ObjectResponse<WarehouseResponseDto> getAllWarehousesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<Warehouse> pages = warehouseRepository.getAllByPartnerId(partnerId, pageable);

        // Lay ra gia tri (content) cua page
        List<Warehouse> warehouses = pages.getContent();

        // Ep kieu sang dto
        List<WarehouseResponseDto> content = warehouses.stream()
                .map(warehouseMapper::toWarehouseResponseDto).toList();

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        ObjectResponse<WarehouseResponseDto> response = new ObjectResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }
}
