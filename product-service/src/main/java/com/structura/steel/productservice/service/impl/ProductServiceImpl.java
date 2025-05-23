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
    public PagingResponse<ProductResponseDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String searchKeyword) {

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
                pages = productSearchRepository.searchByKeyword(searchKeyword, pageable);
            } else {
                pages = productSearchRepository.findAll(pageable);
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
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        validateProductRequest(productRequestDto);

        Product product = productMapper.toProduct(productRequestDto);
        product.setCode(CodeGenerator.generateCode(EntityType.PRODUCT));

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
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        validateProductRequest(productRequestDto);

        productMapper.updateProductFromDto(productRequestDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

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
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(existingProduct);
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
    public List<String> suggest(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = productSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(ProductDocument::getName)   // hoặc .getNameSuggest()
                .distinct()
                .toList();
    }

    private void validateProductRequest(ProductRequestDto dto) {
        ProductType type = dto.type();
        // always require length
        if (dto.length() == null) {
            throw new IllegalArgumentException("Length must not be null.");
        }

        switch (type) {
            case RIBBED_BAR -> {
                require(dto.diameter(), "Diameter must not be null for ribbed bar.");
                // length already checked
            }
            case COIL, PLATE -> {
                require(dto.thickness(), "Thickness must not be null for coil/plate.");
                require(dto.width(),     "Width must not be null for coil/plate.");
            }
            case PIPE, BOX -> {
                require(dto.thickness(), "Thickness must not be null for pipe/box.");
                require(dto.diameter(),  "Diameter must not be null for pipe/box.");
            }
            case SHAPED -> {
                require(dto.unitWeight(), "Unit weight must not be null for shaped steel.");
            }
            default -> { /* no extra */ }
        }
    }

    private void require(Object fieldValue, String message) {
        if (fieldValue == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
