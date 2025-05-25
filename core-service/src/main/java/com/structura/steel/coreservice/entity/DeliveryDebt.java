package com.structura.steel.coreservice.entity;

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

    @Column(name = "order_type")
    private String orderType; // "SALE" hoặc "PURCHASE"

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "debt_note")
    private String debtNote;

    @Column(name = "status")
    private String status;
}
