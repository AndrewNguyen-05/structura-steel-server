package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderDetail extends BaseEntity {

    // Quan hệ với SaleOrder (Core Service)
    @ManyToOne
    @JoinColumn(name = "id_sale_order")
    private SaleOrder saleOrder;

    // Khóa ngoại đến products (Product Service)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "subtotal")
    private BigDecimal subtotal;
}

