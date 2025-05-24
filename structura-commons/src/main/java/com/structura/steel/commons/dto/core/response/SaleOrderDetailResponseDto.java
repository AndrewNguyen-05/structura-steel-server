package com.structura.steel.commons.dto.core.response;

import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class SaleOrderDetailResponseDto {
    private Long id;
    private Long saleOrderId;
    private Long productId;
    private ProductResponseDto product;
    private BigDecimal quantity;
    private BigDecimal weight;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Short version;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
