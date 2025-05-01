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

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    @Column(name = "product_id")
    private Long productId;

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

