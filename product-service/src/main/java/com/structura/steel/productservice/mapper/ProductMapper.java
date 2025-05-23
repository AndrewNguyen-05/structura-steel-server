package com.structura.steel.productservice.mapper;

import com.structura.steel.commons.dto.product.request.ProductRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import com.structura.steel.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {

    Product toProduct(ProductRequestDto productRequestDto);

    ProductResponseDto toProductResponseDto(Product product);

    ProductRequestDto toProductRequestDto(Product product);

    void updateProductFromDto(ProductRequestDto productRequestDto, @MappingTarget Product product);

    // --- Elasticsearch Document ↔ DTO ---
    ProductResponseDto toResponseDtoFromDocument(ProductDocument productDocument);

    ProductDocument toDocument(Product entity);

    Product fromDocument(ProductDocument doc);
}
