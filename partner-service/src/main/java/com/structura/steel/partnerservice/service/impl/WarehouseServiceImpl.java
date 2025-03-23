package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.partnerservice.dto.request.WarehouseRequestDto;
import com.structura.steel.partnerservice.dto.response.WarehouseResponseDto;
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
    public WarehouseResponseDto createWarehouse(WarehouseRequestDto dto) {
        Partner partner = partnerRepository.findById(dto.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.partnerId()));

        Warehouse warehouse = warehouseMapper.toWarehouse(dto);
        warehouse.setPartner(partner);

        Warehouse saved = warehouseRepository.save(warehouse);
        return warehouseMapper.toWarehouseResponseDto(saved);
    }

    @Override
    public WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto dto) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));

        warehouseMapper.updateWarehouseFromDto(dto, existing);
        if (dto.partnerId() != null) {
            Partner partner = partnerRepository.findById(dto.partnerId())
                    .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.partnerId()));
            existing.setPartner(partner);
        }
        Warehouse updated = warehouseRepository.save(existing);
        return warehouseMapper.toWarehouseResponseDto(updated);
    }

    @Override
    public WarehouseResponseDto getWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        return warehouseMapper.toWarehouseResponseDto(warehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        warehouseRepository.delete(warehouse);
    }

    @Override
    public List<WarehouseResponseDto> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouseMapper.toWarehouseResponseDtoList(warehouses);
    }
}
