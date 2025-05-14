package com.structura.steel.dto.response;

import java.time.Instant;
import java.util.List;

public record PartnerResponseDto(
        Long id,
        String partnerType,
        String partnerName,
		String partnerCode,
        String taxCode,
        String legalRepresentative,
        String legalRepresentativePhone,
        String contactPerson,
        String contactPersonPhone,
        String bankName,
        String bankAccountNumber,
        List<PartnerProjectResponseDto> partnerProjects,
        List<VehicleResponseDto> vehicles,
        List<WarehouseResponseDto> warehouses,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
