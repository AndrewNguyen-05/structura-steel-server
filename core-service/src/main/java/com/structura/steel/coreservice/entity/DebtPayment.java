package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.DebtType;
import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "debt_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtPayment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "debt_type", nullable = false)
    private DebtType debtType;

    @Column(name = "debt_id", nullable = false)
    private Long debtId;

    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "notes")
    private String notes;
}