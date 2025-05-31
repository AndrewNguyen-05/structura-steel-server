package com.structura.steel.commons.dto.partner.response;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PartnerProjectResponseDto{
    private Long id;
    private Long partnerId;
    private String projectCode;
    private String projectName;
    private String projectAddress;
    private String projectRepresentative;
    private String projectRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String address;
    private List<ProductResponseDto> products;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}