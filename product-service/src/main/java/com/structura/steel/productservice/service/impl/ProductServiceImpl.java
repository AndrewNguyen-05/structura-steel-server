package com.structura.steel.productservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.commons.dto.product.request.ProductRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import com.structura.steel.productservice.elasticsearch.repository.ProductSearchRepository;
import com.structura.steel.productservice.entity.Product;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.enumeration.ProductType;
import com.structura.steel.productservice.helper.SteelCalculator;
import com.structura.steel.productservice.mapper.ProductMapper;
import com.structura.steel.productservice.repository.ProductRepository;
import com.structura.steel.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;

    private final ProductMapper productMapper;

    @Override
    public PagingResponse<ProductResponseDto> getAllProducts(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String searchKeyword,
            boolean deleted) {

        String effectiveSortBy = sortBy; // Biến tạm để chứa tên trường sort cuối cùng

        // Kiểm tra nếu sortBy là "code" hoặc "name" thì thêm ".keyword"
        if ("code".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "code.keyword";
        } else if ("name".equalsIgnoreCase(sortBy)) {
            effectiveSortBy = "name.keyword";
        }

        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<ProductDocument> pages;

        try {
            if (StringUtils.hasText(searchKeyword)) {
                pages = productSearchRepository.searchByKeyword(searchKeyword, deleted, pageable);
            } else {
                pages = productSearchRepository.findAllByDeleted(deleted, pageable);
            }
        } catch (Exception ex) {
			log.error("Exception: {}", ex.getMessage());
            pages = Page.empty(pageable);
        }

        // Lay ra gia tri (content) cua page
        List<ProductDocument> products = pages.getContent();

        // Ep kieu sang dto
        List<ProductResponseDto> content = products.stream().map(productMapper::toResponseDtoFromDocument).collect(Collectors.toList());

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
    public ProductResponseDto restoreProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        ProductDocument productDocument = productSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setDeleted(false);
        productDocument.setDeleted(false);

        productRepository.save(product);
        productSearchRepository.save(productDocument);

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        validateProductRequest(dto);

        Product product = productMapper.toProduct(dto);
        product.setCode(CodeGenerator.generateCode(EntityType.PRODUCT));
        product.setExportPrice(dto.importPrice().multiply(dto.profitPercentage().add(BigDecimal.valueOf(100)))); // vd gia nhap la 100.000 con % la loi 2% thi se la 100.000 * 102%
        product.setDeleted(false);
        product.setUnitWeight(SteelCalculator.calculateUnitWeight(dto));

        Product savedProduct = productRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(savedProduct);

        // *** GÁN TÊN SẢN PHẨM VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(savedProduct.getName())) {
            productDocument.setSuggestion(savedProduct.getName()); // Chỉ lấy name
        } else {
            productDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        productSearchRepository.save(productDocument); // luu vao Elastic Search

        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        validateProductRequest(dto);

        productMapper.updateProductFromDto(dto, product);
        product.setExportPrice(dto.importPrice().multiply(dto.profitPercentage().add(BigDecimal.valueOf(100))));
        product.setUnitWeight(SteelCalculator.calculateUnitWeight(dto));

        Product updatedProduct = productRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(updatedProduct);

        // *** GÁN TÊN SẢN PHẨM VÀO TRƯỜNG "suggestion" ***
        if (StringUtils.hasText(updatedProduct.getName())) {
            productDocument.setSuggestion(updatedProduct.getName()); // Chỉ lấy name
        } else {
            productDocument.setSuggestion(""); // Hoặc null, để đảm bảo trường được gán
        }

        productSearchRepository.save(productDocument);

        return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public void softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        ProductDocument productDocument = productSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setDeleted(true);
        productDocument.setDeleted(true);

        productRepository.save(product);
        productSearchRepository.save(productDocument);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        productSearchRepository.deleteById(id); // xoa luon trong ES
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

    @Override
    @Transactional(readOnly = true)
    public List<String> suggest(String prefix, int size, boolean deleted) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = productSearchRepository.findBySuggestionPrefix(prefix, deleted, PageRequest.of(0, size));

        return page.getContent().stream()
                .map(ProductDocument::getName)
                .distinct()
                .toList();
    }

    private void validateProductRequest(ProductRequestDto dto) {
        ProductType type = dto.productType();

        // Kiểm tra các trường chung
        if (type == null) {
            throw new IllegalArgumentException("ProductType must not be null.");
        }
        require(dto.length(), "Length must not be null."); // Length bắt buộc cho tất cả

        // Kiểm tra theo từng loại sản phẩm
        switch (type) {
            case RIBBED_BAR, WIRE_COIL -> {
                require(dto.diameter(), "Diameter must not be null for " + type);
                shouldBeNull(dto.width(), "Width", type);
                shouldBeNull(dto.height(), "Height", type);
                shouldBeNull(dto.thickness(), "Thickness", type);
                shouldBeNull(dto.unitWeight(), "UnitWeight", type);
            }
            case COIL, PLATE -> {
                require(dto.width(), "Width must not be null for " + type);
                require(dto.thickness(), "Thickness must not be null for " + type);
                shouldBeNull(dto.height(), "Height", type);
                shouldBeNull(dto.diameter(), "Diameter", type);
                shouldBeNull(dto.unitWeight(), "UnitWeight", type);
            }
            case PIPE -> {
                BigDecimal diameter = require(dto.diameter(), "Diameter must not be null for " + type);
                BigDecimal thickness = require(dto.thickness(), "Thickness must not be null for " + type);
                BigDecimal halfDiameter = diameter.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
                if (thickness.compareTo(halfDiameter) >= 0) {
                    throw new IllegalArgumentException("Thickness cannot be greater than or equal to half the diameter for PIPE.");
                }
                shouldBeNull(dto.width(), "Width", type);
                shouldBeNull(dto.height(), "Height", type);
                shouldBeNull(dto.unitWeight(), "UnitWeight", type);
            }
            case BOX -> {
                BigDecimal width = require(dto.width(), "Width must not be null for " + type);
                BigDecimal height = require(dto.height(), "Height must not be null for " + type);
                BigDecimal thickness = require(dto.thickness(), "Thickness must not be null for " + type);

                BigDecimal halfWidth = width.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
                BigDecimal halfHeight = height.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
                if (thickness.compareTo(halfWidth) >= 0 || thickness.compareTo(halfHeight) >= 0) {
                    throw new IllegalArgumentException("Thickness cannot be greater than or equal to half the width/height for BOX.");
                }
                shouldBeNull(dto.diameter(), "Diameter", type);
                shouldBeNull(dto.unitWeight(), "UnitWeight", type);
            }
            case SHAPED -> {
                require(dto.unitWeight(), "UnitWeight must not be null for " + type);
                shouldBeNull(dto.width(), "Width", type);
                shouldBeNull(dto.height(), "Height", type);
                shouldBeNull(dto.thickness(), "Thickness", type);
                shouldBeNull(dto.diameter(), "Diameter", type);
            }
            default -> throw new UnsupportedOperationException(
                    "Validation not implemented for product type: " + type);
        }
    }

    private <T> T require(T fieldValue, String message) {
        if (fieldValue == null) {
            throw new IllegalArgumentException(message);
        }

        if (fieldValue instanceof BigDecimal && ((BigDecimal) fieldValue).compareTo(BigDecimal.ZERO) <= 0) {
            String positiveMessage = message.replace("must not be null", "must be positive");
            throw new IllegalArgumentException(positiveMessage);
        }
        return fieldValue;
    }

    private void shouldBeNull(Object fieldValue, String fieldName, ProductType type) {
        if (fieldValue != null) {
            throw new IllegalArgumentException(fieldName + " must be null for product type: " + type);
        }
    }

}
