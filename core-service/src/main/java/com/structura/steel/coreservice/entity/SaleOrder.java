package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.PartnerProject;
import com.structura.steel.coreservice.entity.embedded.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "sale_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrder extends BaseEntity {

    @Column(name = "export_code")
    private String exportCode;

    @Column(name = "partner", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Partner partner;

    @Column(name = "project", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private PartnerProject project;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "sale_orders_note")
    private String saleOrdersNote;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL)
    private Set<SaleOrderDetail> saleOrderDetails;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL)
    private Set<SaleDebt> saleDebts;

    private boolean deleted;
}
