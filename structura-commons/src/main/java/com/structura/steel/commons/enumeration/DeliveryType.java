package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryType implements HasText{
    /** Vận chuyển hàng nhập về kho */
    WAREHOUSE_IMPORT("Warehouse Import"),

    /** Vận chuyển hàng giao cho khách hàng/dự án */
    PROJECT_DELIVERY("Project Delivery");

    final String text;

    @Override
    public String text() {
        return text;
    }
}