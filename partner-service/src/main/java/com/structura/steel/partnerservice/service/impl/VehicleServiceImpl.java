package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.commons.dto.partner.request.VehicleRequestDto;
import com.structura.steel.commons.dto.partner.response.VehicleResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.VehicleSearchRepository;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
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
import java.util.stream.Collectors;

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
        Partner partner = getValidPartner(partnerId);

        Vehicle vehicle = vehicleMapper.toVehicle(dto);

        if(vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new DuplicateKeyException("Vehicle", "license plate", vehicle.getLicensePlate());
        }

        vehicle.setPartner(partner);
        vehicle.setVehicleCode(CodeGenerator.generateCode(EntityType.VEHICLE));
        vehicle.setDeleted(false);
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
        getValidPartner(partnerId);
        Vehicle vehicle = getValidVehicle(partnerId, vehicleId, false);

        if (!vehicle.getLicensePlate().equals(dto.licensePlate()) &&
                vehicleRepository.existsByLicensePlate(dto.licensePlate())) {
            throw new DuplicateKeyException("Vehicle", "license plate", vehicle.getLicensePlate());
        }
        vehicleMapper.updateVehicleFromDto(dto, vehicle);
        Vehicle updated = vehicleRepository.save(vehicle);

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
    public VehicleResponseDto getVehicleByPartnerId(Long partnerId, Long vehicleId) {
        getValidPartner(partnerId);

        Vehicle vehicle = getValidVehicle(partnerId, vehicleId, false);

        return vehicleMapper.toVehicleResponseDto(vehicle);
    }

    @Override
    public VehicleResponseDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        if (vehicle.getDeleted()) {
            throw new ResourceNotFoundException("Vehicle", "id", vehicleId + " (is deleted)");
        }
        return vehicleMapper.toVehicleResponseDto(vehicle);
    }


    @Override
    public void deleteVehicle(Long partnerId, Long vehicleId) {
        getValidPartner(partnerId);
        Vehicle vehicle = getValidVehicle(partnerId, vehicleId, true);

        vehicleSearchRepository.deleteById(vehicle.getId());

        vehicleRepository.delete(vehicle);
    }

    @Override
    public void softDeleteVehicle(Long partnerId, Long vehicleId) {
        getValidPartner(partnerId);
        Vehicle vehicle = getValidVehicle(partnerId, vehicleId, false); // Ensure it's not already deleted

        vehicle.setDeleted(true);
        vehicleRepository.save(vehicle);
        saveToElasticsearch(vehicle);
        log.info("Vehicle with ID {} soft-deleted.", vehicleId);
    }

    @Override
    public VehicleResponseDto restoreVehicle(Long partnerId, Long vehicleId) {
        getValidPartner(partnerId);
        Vehicle vehicle = getValidVehicle(partnerId, vehicleId, true); // Find a deleted vehicle

        vehicle.setDeleted(false);
        Vehicle restored = vehicleRepository.save(vehicle);
        saveToElasticsearch(restored);
        log.info("Vehicle with ID {} restored.", vehicleId);

        return vehicleMapper.toVehicleResponseDto(restored);
    }

    @Override
    public PagingResponse<VehicleResponseDto> getAllVehiclesByPartnerId(int pageNo, int pageSize, String sortBy, String sortDir, Long partnerId, String searchKeyword, boolean deleted) {
        getValidPartner(partnerId); // Ensure partner exists

        String effectiveSortBy = sortBy;
        if ("vehicleCode".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "vehicleCode.keyword";
        } else if ("driverName".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "driverName.keyword";
        } else if ("licensePlate".equalsIgnoreCase(sortBy)){
            effectiveSortBy = "licensePlate.keyword";
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<VehicleDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = vehicleSearchRepository.searchByKeywordAndPartnerId(searchKeyword, partnerId, deleted, pageable);
            } else {
                pages = vehicleSearchRepository.getAllByPartnerIdAndDeleted(partnerId, deleted, pageable);
            }
        } catch (Exception ex) {
            log.error("Elasticsearch query failed for vehicles (Partner ID: {}): {}", partnerId, ex.getMessage(), ex);
            pages = Page.empty(pageable);
        }

        List<VehicleResponseDto> content = pages.getContent().stream()
                .map(vehicleMapper::toResponseDtoFromDocument)
                .collect(Collectors.toList()); // Use collect

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
    public List<String> suggestVehicles(String prefix, int size, boolean deleted, Long partnerId) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(0, size);
        Page<VehicleDocument> page = vehicleSearchRepository.findBySuggestionPrefix(prefix, deleted, partnerId, pageable);
        return page.getContent().stream()
                .map(VehicleDocument::getDriverName)
                .distinct()
                .collect(Collectors.toList());
    }

    private Partner getValidPartner(Long partnerId) {
        return partnerRepository.findByIdAndDeletedFalse(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", "id", partnerId + " (or it might be deleted)"));
    }

    private Vehicle getValidVehicle(Long partnerId, Long vehicleId, boolean expectDeleted) {
        // Fetch any vehicle first to give a more specific error
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));

        // Check ownership
        if (!vehicle.getPartner().getId().equals(partnerId)) {
            throw new ResourceNotBelongToException("Vehicle", "id", vehicleId, "partner", "id", partnerId);
        }

        // Check deleted status based on expectation
        if (vehicle.getDeleted() != expectDeleted) {
            throw new ResourceNotFoundException("Vehicle", "id", vehicleId +
                    (expectDeleted ? " (it is not deleted)" : " (it is deleted)"));
        }

        return vehicle;
    }


    private void saveToElasticsearch(Vehicle vehicle) {
        try {
            VehicleDocument vehicleDocument = vehicleMapper.toDocument(vehicle);
            // Suggestion based on license plate and driver name
            String suggestion = (vehicle.getLicensePlate() != null ? vehicle.getLicensePlate() : "") + " "
                    + (vehicle.getDriverName() != null ? vehicle.getDriverName() : "");
            vehicleDocument.setSuggestion(suggestion.trim());
            vehicleDocument.setDeleted(vehicle.getDeleted());
            vehicleSearchRepository.save(vehicleDocument);
            log.info("Vehicle with ID {} saved to Elasticsearch.", vehicle.getId());
        } catch (Exception e) {
            log.error("Error saving vehicle with ID {} to Elasticsearch: {}", vehicle.getId(), e.getMessage(), e);
        }
    }
}
