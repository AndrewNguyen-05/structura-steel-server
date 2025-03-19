package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    @Column(name = "capacity")
    private BigDecimal capacity;

    @Column(name = "description")
    private String description;

    @Column(name = "driver_name")
    private String driverName;

    // Quan hệ với Partner trong cùng Part Service
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}

