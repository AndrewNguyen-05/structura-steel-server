package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partner_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProject extends BaseEntity {

    // Quan hệ với Partner trong cùng Part Service
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_address")
    private String projectAddress;

    @Column(name = "project_representative")
    private String projectRepresentative;

    @Column(name = "project_representative_phone")
    private String projectRepresentativePhone;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(name = "address")
    private String address;
}
