package com.structura.steel.productservice.mapper;

import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import com.structura.steel.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.Date;

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
