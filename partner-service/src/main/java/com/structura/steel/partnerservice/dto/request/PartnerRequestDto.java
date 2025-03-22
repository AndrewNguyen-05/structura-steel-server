package com.structura.steel.partnerservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequestDto {
    private String partnerType; // Đối tác, Nhà cung cấp thép, đơn vị vận chuyển, ...
    private String partnerName;
    private String taxCode;
    private String legalRepresentative;
    private String legalRepresentativePhone;
    private String contactPerson;
    private String contactPersonPhone;
    private String bankName;
    private String bankAccountNumber;
}
