package com.structura.steel.commons.enumeration;

public enum DebtStatus {
    /** Chưa thanh toán */
    UNPAID,

    /** Thanh toán một phần */
    PARTIALLY_PAID,

    /** Đã thanh toán */
    PAID,

    /** Đã hủy (nếu khoản nợ có thể bị hủy) */
    CANCELLED
}
