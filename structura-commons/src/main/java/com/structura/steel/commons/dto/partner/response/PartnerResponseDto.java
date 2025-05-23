package com.structura.steel.commons.dto.partner.response;

import com.structura.steel.commons.enumeration.PartnerType;

import java.time.Instant;
import java.util.List;

public record PartnerResponseDto(
        Long id,
        PartnerType partnerType,
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
