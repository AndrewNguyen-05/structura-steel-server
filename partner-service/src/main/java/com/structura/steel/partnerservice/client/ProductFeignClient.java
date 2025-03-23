package com.structura.steel.partnerservice.client;

import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.partnerservice.client.dto.request.ProductRequestDto;
import com.structura.steel.partnerservice.client.dto.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", path = "/api/products")
public interface ProductFeignClient {

    @GetMapping("/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

    @PostMapping
    ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto);

    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto requestDto);

    @GetMapping
    ObjectResponse<ProductResponseDto> getAllProducts();
}
