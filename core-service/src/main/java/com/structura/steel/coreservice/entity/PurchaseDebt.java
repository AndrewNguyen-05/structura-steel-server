package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Column(name = "product", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Product product;

    @Column(name = "original_amount", nullable = false)
    private BigDecimal originalAmount;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @Column(name = "debt_note")
    private String debtNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DebtStatus status;
}

