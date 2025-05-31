package com.structura.steel.coreservice.entity.embedded;

import java.io.Serializable;
import java.time.Instant;

public record PartnerProject (
        Long id,
        Long partnerId,
        String projectCode,
        String projectName,
        String projectAddress,
        String projectRepresentative,
        String projectRepresentativePhone,
        String contactPerson,
        String contactPersonPhone,
        String address,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) implements Serializable {}
