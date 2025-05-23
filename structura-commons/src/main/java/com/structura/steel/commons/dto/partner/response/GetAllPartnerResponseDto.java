package com.structura.steel.commons.dto.partner.response;

import java.time.Instant;

public record GetAllPartnerResponseDto (
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
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) { }
