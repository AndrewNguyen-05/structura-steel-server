package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Vehicle vehicle = vehicleMapper.toVehicle(dto);

        if(vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new DuplicateKeyException("Vehicle", "license plate", vehicle.getLicensePlate());
        }

        vehicle.setPartner(partner);
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponseDto(saved);
    }

    @Override
    public VehicleResponseDto updateVehicle(Long partnerId, Long vehicleId, VehicleRequestDto dto) {
        // Xác thực Partner
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Vehicle existing = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        if (!existing.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Vehicle", "id", vehicleId, "partner", "id", partnerId);
        }
        if (vehicleRepository.existsByLicensePlate(dto.licensePlate())) {
            throw new DuplicateKeyException("Vehicle", "license plate", existing.getLicensePlate());
        }
        vehicleMapper.updateVehicleFromDto(dto, existing);
        Vehicle updated = vehicleRepository.save(existing);
        return vehicleMapper.toVehicleResponseDto(updated);
    }

    @Override
    public VehicleResponseDto getVehicle(Long partnerId, Long vehicleId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        if (!vehicle.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Vehicle", "id", vehicleId, "partner", "id", partnerId);
        }
        return vehicleMapper.toVehicleResponseDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long partnerId, Long vehicleId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        if (!vehicle.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Vehicle", "id", vehicleId, "partner", "id", partnerId);
        }
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleResponseDto> getAllVehiclesByPartnerId(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        List<Vehicle> vehicles = partner.getVehicles();
        return vehicleMapper.toVehicleResponseDtoList(vehicles);
    }
}
