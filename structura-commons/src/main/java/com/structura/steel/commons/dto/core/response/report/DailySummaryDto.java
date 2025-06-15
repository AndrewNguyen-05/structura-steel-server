package com.structura.steel.commons.dto.core.response.report;

import com.structura.steel.commons.enumeration.ReportOrderType;
import com.structura.steel.commons.enumeration.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record DailySummaryDto(
        // Phần 1: Tóm tắt
        SummarySection summary,
        // Phần 2: Đơn mới
        List<NewOrder> newOrders,
        // Phần 3: Giao hàng hoàn tất
        List<CompletedDelivery> completedDeliveries,
        // Phần 4: Thanh toán
        List<PaymentTransaction> paymentTransactions
) {
    public record SummarySection(
            long newSaleOrdersCount,
            BigDecimal newSaleOrdersValue,
            long newPurchaseOrdersCount,
            long completedDeliveriesCount,
            BigDecimal totalAmountReceived,
            BigDecimal totalAmountPaid
    ) {}

    public record NewOrder(
            Instant createdAt,
            ReportOrderType orderType,
            String orderCode,
            String partnerName,
            BigDecimal value,
            String createdBy
    ) {}

    public record CompletedDelivery(
            String deliveryCode,
            String originalOrderCode,
            String customerName,
            String deliveryAddress,
            Instant completionTime
    ) {}

    public record PaymentTransaction(
            Instant paymentTime,
            TransactionType transactionType,
            String partnerName,
            BigDecimal amount,
            String paymentMethod,
            String notes
    ) {}
}
