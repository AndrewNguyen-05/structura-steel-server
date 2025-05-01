package com.structura.steel.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class SaleOrderDetailResponseDto {
    private Long id;
    private Long saleOrderId;
    private ProductResponseDto product;
    private BigDecimal quantity;
    private BigDecimal weight;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
