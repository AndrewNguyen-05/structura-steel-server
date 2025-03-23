package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.partnerservice.dto.request.VehicleRequestDto;
import com.structura.steel.partnerservice.dto.response.VehicleResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.Vehicle;
import com.structura.steel.partnerservice.mapper.VehicleMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.VehicleRepository;
import com.structura.steel.partnerservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PartnerRepository partnerRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponseDto createVehicle(VehicleRequestDto dto) {
        Partner partner = partnerRepository.findById(dto.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found: " + dto.partnerId()));

        Vehicle vehicle = vehicleMapper.toVehicle(dto);
        vehicle.setPartner(partner);

        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponseDto(saved);
    }

    @Override
    public VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        vehicleMapper.updateVehicleFromDto(dto, existing);
        if (dto.partnerId() != null) {
            Partner partner = partnerRepository.findById(dto.partnerId())
                    .orElseThrow(() -> new RuntimeException("Partner not found: " + dto.partnerId()));
            existing.setPartner(partner);
        }
        Vehicle updated = vehicleRepository.save(existing);
        return vehicleMapper.toVehicleResponseDto(updated);
    }

    @Override
    public VehicleResponseDto getVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        return vehicleMapper.toVehicleResponseDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleResponseDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicleMapper.toVehicleResponseDtoList(vehicles);
    }
}
