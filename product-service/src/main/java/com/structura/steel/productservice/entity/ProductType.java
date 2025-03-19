package com.structura.steel.productservice.entity;

import com.structura.steel.commons.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductType extends BaseEntity {

    @Column(name = "type_code", unique = true, nullable = false)
    private String typeCode;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    // Các thuộc tính liên quan đến trọng lượng thép
    @Column(name = "nominal_diameter_mm")
    private BigDecimal nominalDiameterMm;

    @Column(name = "cross_section_area_mm2")
    private BigDecimal crossSectionAreaMm2;

    @Column(name = "unit_weight_kg_m")
    private BigDecimal unitWeightKgM;

    @Column(name = "standard")
    private String standard;
}
