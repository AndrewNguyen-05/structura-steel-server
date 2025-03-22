package com.structura.steel.productservice.service.impl;

import com.structura.steel.commons.exception.ResourceAlreadyExistException;
import com.structura.steel.commons.response.ObjectResponse;
import com.structura.steel.productservice.dto.request.ProductRequestDto;
import com.structura.steel.productservice.dto.response.ProductResponseDto;
import com.structura.steel.productservice.entity.Product;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.productservice.helper.SteelCalculator;
import com.structura.steel.productservice.mapper.ProductMapper;
import com.structura.steel.productservice.repository.ProductRepository;
import com.structura.steel.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public ObjectResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
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
        ObjectResponse<ProductResponseDto> response = new ObjectResponse<>();
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

        handleProductCodeAlreadyExist(productRequestDto.getCode());

        Product product = productMapper.toProduct(productRequestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        handleProductCodeAlreadyExist(productRequestDto.getCode());

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
    public double calculateWeight(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        return SteelCalculator.calculateSteelWeight(productMapper.toProductRequestDto(product));
    }


    private void validateProductRequest(ProductRequestDto productRequestDto) {
        String name = productRequestDto.getName().toLowerCase();

        if (name.contains("vằn") || name.contains("cây")) {
            if (productRequestDto.getDiameter() == null || productRequestDto.getLength() == null) {
                throw new IllegalArgumentException("Diameter and length must not be null for ribbed bar products.");
            }
        } else if (name.contains("cuộn") || name.contains("tấm")) {
            if (productRequestDto.getThickness() == null || productRequestDto.getWidth() == null || productRequestDto.getLength() == null) {
                throw new IllegalArgumentException("Thickness, width, and length must not be null for coil/plate products.");
            }
        } else if (name.contains("ống") || name.contains("hộp")) {
            if (productRequestDto.getThickness() == null || productRequestDto.getDiameter() == null || productRequestDto.getLength() == null) {
                throw new IllegalArgumentException("Thickness, diameter, and length must not be null for pipe/box products.");
            }
        } else if (name.contains("hình")) {
            if (productRequestDto.getUnitWeight() == null || productRequestDto.getLength() == null) {
                throw new IllegalArgumentException("Unit weight and length must not be null for shaped steel products.");
            }
        }
        if (productRequestDto.getLength() == null) {
            throw new IllegalArgumentException("Length must not be null.");
        }
    }

    private void handleProductCodeAlreadyExist(String code) {
        if(productRepository.existsByCode(code)) {
            throw new ResourceAlreadyExistException("Product", "code", code);
        }
    }
}
