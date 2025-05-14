package com.structura.steel.productservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceAlreadyExistException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.dto.request.ProductRequestDto;
import com.structura.steel.dto.response.ProductResponseDto;
import com.structura.steel.productservice.entity.Product;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.productservice.helper.SteelCalculator;
import com.structura.steel.productservice.mapper.ProductMapper;
import com.structura.steel.productservice.repository.ProductRepository;
import com.structura.steel.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public PagingResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<Product> pages = this.productRepository.findAll(pageable);

        // Lay ra gia tri (content) cua page
        List<Product> products = pages.getContent();


        // Ep kieu sang dto
        List<ProductResponseDto> content = products.stream().map(productMapper::toProductResponseDto).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<ProductResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        validateProductRequest(productRequestDto);

        Product product = productMapper.toProduct(productRequestDto);
        product.setCode(CodeGenerator.generateCode(EntityType.PRODUCT));

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productMapper.updateProductFromDto(productRequestDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(existingProduct);
    }

    @Override
    public ProductResponseDto findByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "code", code));
        return productMapper.toProductResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> findByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateWeight(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        return SteelCalculator.calculateSteelWeight(productMapper.toProductRequestDto(product));
    }

    @Override
    public List<ProductResponseDto> getProductsByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products.stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }


    private void validateProductRequest(ProductRequestDto productRequestDto) {
        String name = productRequestDto.name().toLowerCase();

        if (name.contains("vằn") || name.contains("cây")) {
            if (productRequestDto.diameter() == null || productRequestDto.length() == null) {
                throw new IllegalArgumentException("Diameter and length must not be null for ribbed bar products.");
            }
        } else if (name.contains("cuộn") || name.contains("tấm")) {
            if (productRequestDto.thickness() == null || productRequestDto.width() == null || productRequestDto.length() == null) {
                throw new IllegalArgumentException("Thickness, width, and length must not be null for coil/plate products.");
            }
        } else if (name.contains("ống") || name.contains("hộp")) {
            if (productRequestDto.thickness() == null || productRequestDto.diameter() == null || productRequestDto.length() == null) {
                throw new IllegalArgumentException("Thickness, diameter, and length must not be null for pipe/box products.");
            }
        } else if (name.contains("hình")) {
            if (productRequestDto.unitWeight() == null || productRequestDto.length() == null) {
                throw new IllegalArgumentException("Unit weight and length must not be null for shaped steel products.");
            }
        }
        if (productRequestDto.length() == null) {
            throw new IllegalArgumentException("Length must not be null.");
        }
    }
}
