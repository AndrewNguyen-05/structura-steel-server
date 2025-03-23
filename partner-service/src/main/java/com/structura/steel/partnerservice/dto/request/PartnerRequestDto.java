package com.structura.steel.partnerservice.dto.request;


public record PartnerRequestDto (
    String partnerType, // Đối tác, Nhà cung cấp thép, đơn vị vận chuyển, ...
    String partnerName,
    String taxCode,
    String legalRepresentative,
    String legalRepresentativePhone,
    String contactPerson,
    String contactPersonPhone,
    String bankName,
    String bankAccountNumber
) {}
