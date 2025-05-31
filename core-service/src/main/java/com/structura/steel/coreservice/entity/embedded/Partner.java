package com.structura.steel.coreservice.entity.embedded;

import com.structura.steel.commons.enumeration.PartnerType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record Partner(
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
        BigDecimal debtPayable,
        BigDecimal debtReceivable,
        List<Warehouse> warehouses,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) implements Serializable {}
