package com.structura.steel.partnerservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponseDto {
    private Long id;
    private String partnerType;
    private String partnerName;
    private String taxCode;
    private String legalRepresentative;
    private String legalRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String bankName;
    private String bankAccountNumber;
}
