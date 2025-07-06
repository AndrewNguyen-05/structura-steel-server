package com.structura.steel.productservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import com.structura.steel.commons.enumeration.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal thickness;

    private BigDecimal diameter; // đường kính

    private String standard;

    private BigDecimal importPrice;

    private BigDecimal exportPrice;

    @Column(name = "profit_percentage")
    private BigDecimal profitPercentage;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;
}
