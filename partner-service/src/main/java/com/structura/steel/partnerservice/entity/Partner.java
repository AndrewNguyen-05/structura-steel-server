package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.enumeration.PartnerType;
import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partner extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_type", nullable = false)
    private PartnerType partnerType;

    @Column(name = "partner_name", nullable = false)
    private String partnerName;

    @Column(name = "partner_code", nullable = false)
    private String partnerCode;

    @Column(name = "tax_code", nullable = false)
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

    @Column(name = "debt_payable", precision = 19, scale = 4, nullable = false)
    private BigDecimal debtPayable = BigDecimal.ZERO;

    @Column(name = "debt_receivable", precision = 19, scale = 4, nullable = false)
    private BigDecimal debtReceivable = BigDecimal.ZERO;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnerProject> partnerProjects;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Warehouse> warehouses;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;
}

