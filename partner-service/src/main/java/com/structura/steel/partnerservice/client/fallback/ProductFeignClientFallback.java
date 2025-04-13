package com.structura.steel.partnerservice.client.fallback;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.partnerservice.client.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class ProductFeignClientFallback implements ProductFeignClient {

    @Override
    public ProductResponseDto getProductById(Long id) {
        // 1) Log ra để biết service đang down
        log.error("Product Service is down, fallback method getProductById is called");

        // 2) Tạo object giả lập trả về
        return createDummyProduct();
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        log.error("Product Service is down, fallback method createProduct is called");
        return createDummyProduct();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        log.error("Product Service is down, fallback method updateProduct is called");
        return createDummyProduct();
    }

    @Override
    public PagingResponse<ProductResponseDto> getAllProducts() {
        log.error("Product Service is down, fallback method getAllProducts is called");

        // Trả về 1 PagingResponse dummy
        PagingResponse<ProductResponseDto> response = new PagingResponse<>();
        response.setContent(new ArrayList<>());
        response.setPageNo(0);
        response.setPageSize(0);
        response.setTotalPages(0);
        response.setTotalElements(0);
        response.setLast(true);
        return response;
    }

    private ProductResponseDto createDummyProduct() {
        return new ProductResponseDto(-1L, "product code","Dummy product - service down", null, null, null, null, null, null, null);
    }
}
