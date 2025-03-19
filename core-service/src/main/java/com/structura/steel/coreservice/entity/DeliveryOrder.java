package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "delivery_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrder extends BaseEntity {

    // Quan hệ với PurchaseOrder (Core Service)
    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // Quan hệ với SaleOrder (Core Service)
    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    // Khóa ngoại đến vehicles (Part Service)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "driver_name")
    private String driverName;

    // Khóa ngoại đến warehouses (Part Service)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "confirmation_from_partner")
    private String confirmationFromPartner;

    @Column(name = "confirmation_from_factory")
    private String confirmationFromFactory;

    @Column(name = "distance")
    private BigDecimal distance;

    @Column(name = "delivery_unit_price")
    private BigDecimal deliveryUnitPrice;

    @Column(name = "additional_fees")
    private BigDecimal additionalFees;

    @Column(name = "total_delivery_fee")
    private BigDecimal totalDeliveryFee;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "delivery_order_note")
    private String deliveryOrderNote;
}
