package com.structura.steel.partnerservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerProjectResponseDto {
    private Long id;
    private Long partnerId;
    private String projectName;
    private String projectAddress;
    private String projectRepresentative;
    private String projectRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String address;

    private List<Long> productIds;
}
