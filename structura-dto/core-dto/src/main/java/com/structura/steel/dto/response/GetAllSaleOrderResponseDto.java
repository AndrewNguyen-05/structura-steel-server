package com.structura.steel.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record GetAllSaleOrderResponseDto(
    Long id,
    Long partnerId,
    Long projectId,
    Instant orderDate,
    UserResponse user,
    String status,
    String orderType,
    BigDecimal totalAmount,
    String saleOrdersNote,
    Set<SaleOrderDetailResponseDto>saleOrderDetails,
    Set<SaleDebtResponseDto> saleDebts
) {}
