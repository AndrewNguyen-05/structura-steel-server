package com.structura.steel.productservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private BigDecimal unitWeight;

    @Column(name = "length", nullable = false)
    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal thickness;

    private BigDecimal diameter; // đường kính

    private String standard;
}
