package com.structura.steel.partnerservice.client;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.partnerservice.client.fallback.ProductFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8020/product-service",
        fallback = ProductFeignClientFallback.class
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
}
