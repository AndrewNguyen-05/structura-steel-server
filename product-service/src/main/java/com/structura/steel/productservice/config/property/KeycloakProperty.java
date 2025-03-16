package com.structura.steel.productservice.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak")
public record KeycloakProperty(
        String url,
        Realm realm,
        Client client,
        Admin admin
) {
    public record Realm(
            String master,
            String structura
    ) {}

    public record Client(
            String adminCli,
            String structura
    ) {}

    public record Admin(
            String username,
            String password
    ) {}
}
