package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partner extends BaseEntity {

    @Column(name = "partner_type")
    private String partnerType; // "Cá nhân", "Dự án", "Đại lý"

    @Column(name = "partner_name", nullable = false)
    private String partnerName;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "legal_representative")
    private String legalRepresentative;

    @Column(name = "legal_representative_phone")
    private String legalRepresentativePhone;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;
}

