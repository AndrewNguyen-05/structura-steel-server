package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportOrderType implements HasText {

    SALE("Bán hàng"),
    PURCHASE("Mua hàng"),
    DELIVERY("Giao hàng");

    final String text;

    @Override
    public String text() {
        return text;
    }
}