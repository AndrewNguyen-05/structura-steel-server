package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "delivery_debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDebt extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "delivery_order_id")
    private DeliveryOrder deliveryOrder;

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
