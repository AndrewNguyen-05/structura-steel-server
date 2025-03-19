package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sale_debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDebt extends BaseEntity {

    // Quan hệ với SaleOrder (Core Service)
    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    // Khóa ngoại đến partner_projects (Part Service)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "order_type")
    private String orderType; // "SALE" hoặc "PURCHASE"

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "paid_date")
    private Instant paidDate;

    @Column(name = "debt_note")
    private String debtNote;

    @Column(name = "status")
    private String status;
}

