package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.Warehouse;
import com.structura.steel.partnerservice.mapper.WarehouseMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
import com.structura.steel.partnerservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
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
    public List<WarehouseResponseDto> getAllWarehousesByPartnerId(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        List<Warehouse> warehouses = partner.getWarehouses();
        return warehouseMapper.toWarehouseResponseDtoList(warehouses);
    }
}
