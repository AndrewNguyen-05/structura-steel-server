package com.structura.steel.dto.request;

import java.time.Instant;
import java.math.BigDecimal;

public record SaleOrderRequestDto(
        Long partnerId,
        Long projectId,
        Instant orderDate,
        String userId,
        String status,
        String orderType,
        BigDecimal totalAmount,
        String saleOrdersNote
) {}

