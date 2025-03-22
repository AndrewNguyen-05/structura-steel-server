package com.structura.steel.productservice.service;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.productservice.dto.request.ProductRequestDto;
import com.structura.steel.productservice.dto.response.ProductResponseDto;
import java.util.List;

public interface ProductService {
    ObjectResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);
    ProductResponseDto findByCode(String code);
    List<ProductResponseDto> findByName(String name);
    double calculateWeight(Long productId);
}
