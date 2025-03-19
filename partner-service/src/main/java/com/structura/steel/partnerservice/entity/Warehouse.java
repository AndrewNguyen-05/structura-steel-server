package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseEntity {

    @Column(name = "warehouse_name", nullable = false)
    private String warehouseName;

    @Column(name = "warehouse_address")
    private String warehouseAddress;

    // Quan hệ với Partner trong cùng Part Service
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}

