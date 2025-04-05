package com.structura.steel.productservice.mapper;

import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {
    Product toProduct(ProductRequestDto productRequestDto);
    ProductResponseDto toProductResponseDto(Product product);
    ProductRequestDto toProductRequestDto(Product product);
    void updateProductFromDto(ProductRequestDto productRequestDto, @MappingTarget Product product);
}
