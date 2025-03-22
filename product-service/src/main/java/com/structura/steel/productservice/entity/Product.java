package com.structura.steel.productservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit_weight")
    private Double unitWeight;

    @Column(name = "length", nullable = false)
    private Double length;

    private Double width;

    private Double thickness;

    private Double diameter; // đường kính

    private String standard;
}
