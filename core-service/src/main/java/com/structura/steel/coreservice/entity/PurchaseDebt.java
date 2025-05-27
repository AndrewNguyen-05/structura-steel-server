package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "purchase_debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDebt extends BaseEntity {

    // Quan hệ với PurchaseOrder (Core Service)
    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // Khóa ngoại đến partner_projects (Part Service)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "original_amount", nullable = false)
    private BigDecimal originalAmount;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @Column(name = "debt_note")
    private String debtNote;

    @Column(name = "status")
    private DebtStatus status;
}

