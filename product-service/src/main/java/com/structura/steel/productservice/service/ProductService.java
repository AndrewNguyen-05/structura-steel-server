package com.structura.steel.productservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    PagingResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);
    ProductResponseDto findByCode(String code);
    List<ProductResponseDto> findByName(String name);
    BigDecimal calculateWeight(Long productId);
}
