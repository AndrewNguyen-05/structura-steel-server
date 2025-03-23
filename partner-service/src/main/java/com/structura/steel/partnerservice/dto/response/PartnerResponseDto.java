package com.structura.steel.partnerservice.dto.response;

public record PartnerResponseDto (
    Long id,
    String partnerType,
    String partnerName,
    String taxCode,
    String legalRepresentative,
    String legalRepresentativePhone,
    String contactPerson,
    String contactPersonPhone,
    String bankName,
    String bankAccountNumber
) {}
