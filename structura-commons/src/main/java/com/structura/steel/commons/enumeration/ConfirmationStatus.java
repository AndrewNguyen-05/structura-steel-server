package com.structura.steel.commons.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConfirmationStatus implements HasText{
    YES("Yes"),
    PENDING("Pending"),
    NO("No");

    final String text;

    @Override
    public String text() {
        return text;
    }
}
