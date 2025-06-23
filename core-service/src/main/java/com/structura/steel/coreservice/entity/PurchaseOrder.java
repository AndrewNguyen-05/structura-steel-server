package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.ConfirmationStatus;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.PartnerProject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder extends BaseEntity {

    @Column(name = "import_code")
    private String importCode;

    @Column(name = "supplier", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Partner supplier;

    @Column(name = "project", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private PartnerProject project;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmation_from_supplier")
    private ConfirmationStatus confirmationFromSupplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_weight")
    private BigDecimal totalWeight;

    @Column(name = "purchase_orders_note")
    private String purchaseOrdersNote;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseOrderDetail> purchaseOrderDetails;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseDebt> purchaseDebts;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DeliveryOrder> deliveryOrders = new HashSet<>();

    private boolean deleted = false;
}
