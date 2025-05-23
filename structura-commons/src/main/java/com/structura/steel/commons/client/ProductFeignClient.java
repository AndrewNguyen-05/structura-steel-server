package com.structura.steel.commons.client;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.product.request.ProductRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8020/product-service/products"
)
public interface ProductFeignClient {

    @GetMapping("/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

    @PostMapping
    ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto);

    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto requestDto);

    @GetMapping
    PagingResponse<ProductResponseDto> getAllProducts();

    @GetMapping("/batch")
    List<ProductResponseDto> getProductsBatch(@RequestParam("ids") List<Long> ids);

    @GetMapping("/{id}/weight")
    BigDecimal getProductWeight(@PathVariable Long id);
}
