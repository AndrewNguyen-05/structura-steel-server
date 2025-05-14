package com.structura.steel.partnerservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "vehicle_code")
    private String vehicleCode;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    @Column(name = "capacity")
    private Double capacity;

    @Column(name = "description")
    private String description;

    @Column(name = "driver_name")
    private String driverName;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
}

