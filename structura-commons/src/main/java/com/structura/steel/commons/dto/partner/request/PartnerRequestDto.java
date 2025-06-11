package com.structura.steel.commons.dto.partner.request;

import com.structura.steel.commons.enumeration.PartnerType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;

public record PartnerRequestDto(

        @NotNull(message = "Partner productType must not be null")
        PartnerType partnerType,

        @NotBlank(message = "Partner name must not be null")
        String partnerName,

        @NotBlank(message = "Partner tax code must not be empty")
        String taxCode,

        String legalRepresentative,

        @Pattern(
                regexp = "^(0|\\+84)(\\d{9})$",
                message = "Phone number not valid"
        )
        String legalRepresentativePhone,
        String contactPerson,

        @Pattern(
                regexp = "^(0|\\+84)(\\d{9})$",
                message = "Phone number not valid"
        )
        String contactPersonPhone,
        String bankName,
        String bankAccountNumber,
        BigDecimal debtPayable,
        BigDecimal debtReceivable,
        List<PartnerProjectRequestDto> partnerProjects,
        List<VehicleRequestDto> vehicles,
        List<WarehouseRequestDto> warehouses
) {}
