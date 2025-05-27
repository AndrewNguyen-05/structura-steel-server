package com.structura.steel.commons.dto.partner.request;

import com.structura.steel.commons.enumeration.PartnerType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record PartnerRequestDto(
        @NotNull(message = "Partner productType must not be null")
        PartnerType partnerType,

        @NotNull(message = "Partner name must not be null")
        String partnerName,
        String taxCode,
        String legalRepresentative,
        String legalRepresentativePhone,
        String contactPerson,
        String contactPersonPhone,
        String bankName,
        String bankAccountNumber,
        BigDecimal debtPayable,
        BigDecimal debtReceivable,
        List<PartnerProjectRequestDto> partnerProjects,
        List<VehicleRequestDto> vehicles,
        List<WarehouseRequestDto> warehouses
) {}
