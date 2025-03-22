package com.structura.steel.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String code;
    private String name;
    private Double unitWeight;
    private Double length;
    private Double width;
    private Double thickness;
    private Double diameter;
    private String standard;
}
