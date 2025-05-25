package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "product_id")
    private Long productId;

    private BigDecimal originalAmount;
    private BigDecimal remainingAmount;

    @Column(name = "debt_note")
    private String debtNote;

    @Column(name = "status")
    private String status;
}

