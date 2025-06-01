package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.coreservice.entity.embedded.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    Product toProductSnapShot(ProductResponseDto dto);

    ProductResponseDto toProductResponseDto(Product product);
}
