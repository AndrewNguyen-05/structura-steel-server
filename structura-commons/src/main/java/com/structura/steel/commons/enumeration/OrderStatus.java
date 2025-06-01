package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus implements HasText{
    NEW("New"),
    PROCESSING("Processing"),
    IN_TRANSIT("In transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    DONE("Done");

    final String text;

    @Override
    public String text() {
        return text;
    }
}
