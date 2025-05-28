package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

public enum DebtStatus implements HasText {
    /** Chưa thanh toán */
    UNPAID("Unpaid"),

    /** Thanh toán một phần */
    PARTIALLY_PAID("Partially paid"),

    /** Đã thanh toán */
    PAID("Paid"),

    /** Đã hủy (nếu khoản nợ có thể bị hủy) */
    CANCELLED("Cancelled");

    final String text;

    DebtStatus(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }
}
