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
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateToInstant")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "dateToInstant")
    ProductResponseDto toResponseDtoFromDocument(ProductDocument productDocument);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "instantToDate")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "instantToDate")
    ProductDocument toDocument(Product entity);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateToInstant")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "dateToInstant")
    Product fromDocument(ProductDocument doc);

    // --- Converters Instant ↔ Date ---
    @Named("instantToDate")
    static Date instantToDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    @Named("dateToInstant")
    static Instant dateToInstant(Date date) {
        return date == null ? null : date.toInstant();
    }
}
