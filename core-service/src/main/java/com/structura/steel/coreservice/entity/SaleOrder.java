package com.structura.steel.coreservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "sale_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrder extends BaseEntity {

    // Khóa ngoại đến partners (Part Service) → lưu plain field
    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    // Khóa ngoại đến partner_projects (Part Service)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "order_date")
    private Instant orderDate;

    // Quan hệ với User trong cùng Core Service
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private String status; // "Đã tạo", "Đã xác nhận", etc.

    @Column(name = "order_type")
    private String orderType; // Bán lẻ, Bán nguyên cuộn

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "sale_orders_note")
    private String saleOrdersNote;

    // Quan hệ 1-n với SaleOrderDetail
    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL)
    private Set<SaleOrderDetail> saleOrderDetails;
}
