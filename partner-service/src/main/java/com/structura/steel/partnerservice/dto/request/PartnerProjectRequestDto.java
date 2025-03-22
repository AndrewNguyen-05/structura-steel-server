package com.structura.steel.partnerservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerProjectRequestDto {
    private Long partnerId;
    private String projectName;
    private String projectAddress;
    private String projectRepresentative;
    private String projectRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String address;

    // Danh sách productId mà client chọn
    private List<Long> productIds;
}
