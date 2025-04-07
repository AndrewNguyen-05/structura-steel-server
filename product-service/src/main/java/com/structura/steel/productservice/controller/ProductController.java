package com.structura.steel.productservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<PagingResponse<ProductResponseDto>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productService.createProduct(productRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(productService.findByCode(code));
    }

    @GetMapping("/name")
    public ResponseEntity<List<ProductResponseDto>> getProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/{id}/weight")
    public ResponseEntity<BigDecimal> getProductWeight(@PathVariable Long id) {
        return ResponseEntity.ok(productService.calculateWeight(id));
    }
}