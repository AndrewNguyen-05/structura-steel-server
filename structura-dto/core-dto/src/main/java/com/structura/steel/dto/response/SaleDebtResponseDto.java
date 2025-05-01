package com.structura.steel.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class SaleDebtResponseDto {
    private Long id;
    private Long saleOrderId;
    private ProductResponseDto product;
    private BigDecimal amount;
    private Instant paymentDate;
    private Instant paidDate;
    private String debtNote;
    private String status;
};