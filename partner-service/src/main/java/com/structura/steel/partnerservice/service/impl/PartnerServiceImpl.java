package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.partnerservice.dto.request.PartnerRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.entity.Vehicle;
import com.structura.steel.partnerservice.entity.Warehouse;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.mapper.VehicleMapper;
import com.structura.steel.partnerservice.mapper.WarehouseMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.VehicleRepository;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
import com.structura.steel.partnerservice.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerProjectRepository partnerProjectRepository;
    private final VehicleRepository vehicleRepository;
    private final WarehouseRepository warehouseRepository;

    private final PartnerMapper partnerMapper;
    private final PartnerProjectMapper partnerProjectMapper;
    private final VehicleMapper vehicleMapper;
    private final WarehouseMapper warehouseMapper;

    @Override
    public PartnerResponseDto createPartner(PartnerRequestDto dto) {
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

        Partner savedPartner = partnerRepository.save(partner);

        // Tạo và lưu các PartnerProject (nếu có)
        if (dto.partnerProjects() != null && !dto.partnerProjects().isEmpty()) {
            List<PartnerProject> projects = dto.partnerProjects().stream().map(projectDto -> {
                PartnerProject project = partnerProjectMapper.toPartnerProject(projectDto);
                project.setPartner(savedPartner);
                return partnerProjectRepository.save(project);
            }).collect(Collectors.toList());
            savedPartner.setPartnerProjects(projects);
        }

        // Tạo và lưu Vehicle (nếu có)
        if (dto.vehicles() != null && !dto.vehicles().isEmpty()) {
            List<Vehicle> vehicles = dto.vehicles().stream().map(vehicleDto -> {
                if(vehicleRepository.existsByLicensePlate(vehicleDto.licensePlate())) {
                    throw new DuplicateKeyException("Vehicle", "licensePlate", vehicleDto.licensePlate());
                }
                Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);
                vehicle.setPartner(savedPartner);
                return vehicleRepository.save(vehicle);
            }).collect(Collectors.toList());
            savedPartner.setVehicles(vehicles);
        }

        // Tạo và lưu Warehouse (nếu có)
        if (dto.warehouses() != null && !dto.warehouses().isEmpty()) {
            List<Warehouse> warehouses = dto.warehouses().stream().map(warehouseDto -> {
                Warehouse warehouse = warehouseMapper.toWarehouse(warehouseDto);
                warehouse.setPartner(savedPartner);
                return warehouseRepository.save(warehouse);
            }).collect(Collectors.toList());
            savedPartner.setWarehouses(warehouses);
        }

        return partnerMapper.toPartnerResponseDto(savedPartner);
    }

    @Override
    public PartnerResponseDto updatePartner(Long id, PartnerRequestDto dto) {
        Partner existing = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));

        // Cập nhật các trường của Partner
        existing.setPartnerType(dto.partnerType());
        existing.setPartnerName(dto.partnerName());
        existing.setTaxCode(dto.taxCode());
        existing.setLegalRepresentative(dto.legalRepresentative());
        existing.setLegalRepresentativePhone(dto.legalRepresentativePhone());
        existing.setContactPerson(dto.contactPerson());
        existing.setContactPersonPhone(dto.contactPersonPhone());
        existing.setBankName(dto.bankName());
        existing.setBankAccountNumber(dto.bankAccountNumber());

        // Với các nested entity, giải pháp đơn giản: xoá toàn bộ entity con hiện có và tạo mới theo DTO
        if (existing.getPartnerProjects() != null) {
            existing.getPartnerProjects().forEach(partnerProjectRepository::delete);
        }
        if (dto.partnerProjects() != null && !dto.partnerProjects().isEmpty()) {
            List<PartnerProject> projects = dto.partnerProjects().stream().map(projectDto -> {
                PartnerProject project = partnerProjectMapper.toPartnerProject(projectDto);
                project.setPartner(existing);
                return partnerProjectRepository.save(project);
            }).collect(Collectors.toList());
            existing.setPartnerProjects(projects);
        }

        if (existing.getVehicles() != null) {
            existing.getVehicles().forEach(vehicleRepository::delete);
        }
        if (dto.vehicles() != null && !dto.vehicles().isEmpty()) {
            List<Vehicle> vehicles = dto.vehicles().stream().map(vehicleDto -> {
                Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);
                vehicle.setPartner(existing);
                return vehicleRepository.save(vehicle);
            }).collect(Collectors.toList());
            existing.setVehicles(vehicles);
        }

        if (existing.getWarehouses() != null) {
            existing.getWarehouses().forEach(warehouseRepository::delete);
        }
        if (dto.warehouses() != null && !dto.warehouses().isEmpty()) {
            List<Warehouse> warehouses = dto.warehouses().stream().map(warehouseDto -> {
                Warehouse warehouse = warehouseMapper.toWarehouse(warehouseDto);
                warehouse.setPartner(existing);
                return warehouseRepository.save(warehouse);
            }).collect(Collectors.toList());
            existing.setWarehouses(warehouses);
        }

        Partner updated = partnerRepository.save(existing);
        return partnerMapper.toPartnerResponseDto(updated);
    }

    @Override
    public PartnerResponseDto getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));
        return partnerMapper.toPartnerResponseDto(partner);
    }

    @Override
    public void deletePartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", id));

        // Xoá các entity con nếu cascade chưa được cấu hình
        if (partner.getPartnerProjects() != null) {
            partnerProjectRepository.deleteAll(partner.getPartnerProjects());
        }
        if (partner.getVehicles() != null) {
            vehicleRepository.deleteAll(partner.getVehicles());
        }
        if (partner.getWarehouses() != null) {
            warehouseRepository.deleteAll(partner.getWarehouses());
        }
        partnerRepository.delete(partner);
    }

    @Override
    public List<PartnerResponseDto> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        return partners.stream().map(partnerMapper::toPartnerResponseDto).collect(Collectors.toList());
    }
}
