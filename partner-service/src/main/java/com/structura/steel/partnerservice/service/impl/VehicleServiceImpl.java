package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;
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
    public VehicleResponseDto createVehicle(Long partnerId, VehicleRequestDto dto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        Vehicle vehicle = vehicleMapper.toVehicle(dto);
        vehicle.setPartner(partner);
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponseDto(saved);
    }

    @Override
    public VehicleResponseDto updateVehicle(Long partnerId, Long vehicleId, VehicleRequestDto dto) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        Vehicle existing = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        if (!existing.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("Vehicle id " + vehicleId + " not belong to Partner id " + partnerId);
        }
        vehicleMapper.updateVehicleFromDto(dto, existing);
        Vehicle updated = vehicleRepository.save(existing);
        return vehicleMapper.toVehicleResponseDto(updated);
    }

    @Override
    public VehicleResponseDto getVehicle(Long partnerId, Long vehicleId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        if (!vehicle.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("Vehicle id " + vehicleId + " not belong to Partner id " + partnerId);
        }
        return vehicleMapper.toVehicleResponseDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long partnerId, Long vehicleId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        if (!vehicle.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("Vehicle id " + vehicleId + " not belong to Partner id " + partnerId);
        }
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleResponseDto> getAllVehiclesByPartnerId(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));
        List<Vehicle> vehicles = partner.getVehicles();
        return vehicleMapper.toVehicleResponseDtoList(vehicles);
    }
}
