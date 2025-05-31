package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDebt extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

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

