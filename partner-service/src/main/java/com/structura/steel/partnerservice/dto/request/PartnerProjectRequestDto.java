package com.structura.steel.partnerservice.dto.request;

import java.util.List;

public record PartnerProjectRequestDto (
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
