package com.structura.steel.partnerservice.dto.response;

import java.util.List;

public record PartnerProjectResponseDto (
    Long id,
    Long partnerId,
    String projectName,
    String projectAddress,
    String projectRepresentative,
    String projectRepresentativePhone,
    String contactPerson,
    String contactPersonPhone,
    String address,

    List<Long> productIds
) {}
