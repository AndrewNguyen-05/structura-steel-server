package com.structura.steel.productservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.product.request.ProductRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    PagingResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String searchKeyword);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

    BigDecimal calculateWeight(Long productId);

    List<ProductResponseDto> getProductsByIds(List<Long> ids);

    List<String> suggest(String prefix, int size);
}
