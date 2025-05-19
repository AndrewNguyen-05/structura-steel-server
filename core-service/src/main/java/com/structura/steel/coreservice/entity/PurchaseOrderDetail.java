package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    // Khóa ngoại đến products (Product Service)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "subtotal")
    private BigDecimal subtotal;
}

