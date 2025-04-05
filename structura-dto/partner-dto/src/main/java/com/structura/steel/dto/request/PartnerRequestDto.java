package com.structura.steel.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PartnerRequestDto(
        String partnerType,

        @NotNull(message = "Partner name must not be null")
        String partnerName,
        String taxCode,
        String legalRepresentative,
        String legalRepresentativePhone,
        String contactPerson,
        String contactPersonPhone,
        String bankName,
        String bankAccountNumber,
        List<PartnerProjectRequestDto> partnerProjects,
        List<VehicleRequestDto> vehicles,
        List<WarehouseRequestDto> warehouses
) {}
