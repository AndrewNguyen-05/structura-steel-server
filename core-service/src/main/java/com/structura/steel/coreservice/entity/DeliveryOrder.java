package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.ConfirmationStatus;
import com.structura.steel.commons.enumeration.DeliveryType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "delivery_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrder extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    @Column(name = "delivery_code")
    private String deliveryCode;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "partner", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Partner partner;

    @Column(name = "vehicle", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Vehicle vehicle;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmation_from_partner")
    private ConfirmationStatus confirmationFromPartner;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmation_from_factory")
    private ConfirmationStatus confirmationFromFactory;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmation_from_receiver")
    private ConfirmationStatus confirmationFromReceiver;

    @Column(name = "distance")
    private BigDecimal distance;

    @Column(name = "delivery_unit_price")
    private BigDecimal deliveryUnitPrice;

    @Column(name = "additional_fees")
    private BigDecimal additionalFees = new BigDecimal(0);

    @Column(name = "total_delivery_fee")
    private BigDecimal totalDeliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "delivery_order_note")
    private String deliveryOrderNote;

    @OneToMany(mappedBy = "deliveryOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeliveryDebt> deliveryDebts;

    private boolean deleted = false;
}
