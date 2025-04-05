package com.structura.steel.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PartnerProjectResponseDto{
    private Long id;
    private Long partnerId;
    private String projectName;
    private String projectAddress;
    private String projectRepresentative;
    private String projectRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String address;
    private List<ProductResponseDto> products;
}