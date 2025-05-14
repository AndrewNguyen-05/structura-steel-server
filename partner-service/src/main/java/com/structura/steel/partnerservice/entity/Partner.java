package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


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

    @Column(name = "partner_code", nullable = false)
    private String partnerCode;

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

    @OneToMany(mappedBy = "partner")
    private List<PartnerProject> partnerProjects;

    @OneToMany(mappedBy = "partner")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "partner")
    private List<Warehouse> warehouses;
}

