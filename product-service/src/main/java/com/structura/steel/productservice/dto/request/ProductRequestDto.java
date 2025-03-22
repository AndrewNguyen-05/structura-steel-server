package com.structura.steel.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Code is mandatory")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    private Double unitWeight;

    @NotNull(message = "Length cannot be null")
    private Double length;
    private Double width;
    private Double thickness;
    private Double diameter;
    private String standard;
}