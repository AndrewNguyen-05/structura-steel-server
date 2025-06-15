package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionType implements HasText {

    INCOME("Thu"),
    EXPENSE("Chi");

    final String text;

    @Override
    public String text() {
        return text;
    }
}
