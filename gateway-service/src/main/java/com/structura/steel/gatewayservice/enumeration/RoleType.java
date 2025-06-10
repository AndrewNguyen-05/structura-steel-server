package com.structura.steel.gatewayservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType implements HasText {
    ADMIN("ROLE_ADMIN"),
    IMPORTER("ROLE_IMPORTER"),
    SALER("ROLE_SALER"),
    ACCOUNTANT("ROLE_ACCOUNTANT");

    final String name;

    @Override
    public String text() {
        return name;
    }
}
