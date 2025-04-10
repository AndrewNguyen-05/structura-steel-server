package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Table(name = "partner_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProject extends BaseEntity {

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

    private String address;

    @Column(name = "product_ids", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> productIds;
}
