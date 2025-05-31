package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.coreservice.entity.embedded.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDetail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @Column(name = "product", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Product product;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "subtotal")
    private BigDecimal subtotal;
}

