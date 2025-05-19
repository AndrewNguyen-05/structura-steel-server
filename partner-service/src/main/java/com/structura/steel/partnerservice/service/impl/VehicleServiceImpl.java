package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.VehicleSearchRepository;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.Vehicle;
import com.structura.steel.partnerservice.mapper.VehicleMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.repository.VehicleRepository;
import com.structura.steel.partnerservice.service.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PartnerRepository partnerRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleSearchRepository vehicleSearchRepository;

    @Override
    public VehicleResponseDto createVehicle(Long partnerId, VehicleRequestDto dto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));
        Vehicle vehicle = vehicleMapper.toVehicle(dto);

        if(vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new DuplicateKeyException("Vehicle", "license plate", vehicle.getLicensePlate());
        }

        vehicle.setPartner(partner);
        vehicle.setVehicleCode(CodeGenerator.generateCode(EntityType.VEHICLE));
        Vehicle saved = vehicleRepository.save(vehicle);

        VehicleDocument vehicleDocument = vehicleMapper.toDocument(saved);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(saved.getPartner().getPartnerName())) {
            vehicleDocument.setSuggestion(saved.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            vehicleDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        vehicleSearchRepository.save(vehicleDocument); // luu vao Elastic Search

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

        VehicleDocument vehicleDocument = vehicleMapper.toDocument(updated);

        // *** GÁN TÊN VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(updated.getPartner().getPartnerName())) {
            vehicleDocument.setSuggestion(updated.getPartner().getPartnerName()); // Chỉ lấy name
        } else {
            vehicleDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        vehicleSearchRepository.save(vehicleDocument); // luu vao Elastic Search

        return vehicleMapper.toVehicleResponseDto(updated);
    }

    @Override
    public VehicleResponseDto getVehicleById(Long partnerId, Long vehicleId) {
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
        vehicleSearchRepository.deleteById(vehicle.getId()); // luu vao Elastic Search

        vehicleRepository.delete(vehicle);
    }

    @Override
    public PagingResponse<VehicleResponseDto> getAllVehiclesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId));

        String effectiveSortBy = sortBy;
        if ("vehicleCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "vehicleCode.keyword";
        } else if ("driverName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "driverName.keyword";
        }

        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<VehicleDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = vehicleSearchRepository.searchByKeyword(searchKeyword, pageable);
            } else {
                pages = vehicleSearchRepository.findAll(pageable);
            }
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<VehicleDocument> vehicles = pages.getContent();

        // Ep kieu sang dto
        List<VehicleResponseDto> content = vehicles.stream()
                .map(vehicleMapper::toResponseDtoFromDocument).toList();

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<VehicleResponseDto> response = new PagingResponse<>();
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
    public List<String> suggestVehicles(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = vehicleSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(VehicleDocument::getDriverName)
                .distinct()
                .toList();
    }
}
