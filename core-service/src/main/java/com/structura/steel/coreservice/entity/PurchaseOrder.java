package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder extends BaseEntity {

    // Khóa ngoại đến partners (Part Service)
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    // Khóa ngoại đến partner_projects (Part Service)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "order_date")
    private Instant orderDate;

    // Quan hệ với User (Core Service)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "purchase_orders_note")
    private String purchaseOrdersNote;

    // Khóa ngoại đến warehouses (Part Service)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    // Quan hệ 1-n với PurchaseOrderDetail
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private Set<PurchaseOrderDetail> purchaseOrderDetails;
}
