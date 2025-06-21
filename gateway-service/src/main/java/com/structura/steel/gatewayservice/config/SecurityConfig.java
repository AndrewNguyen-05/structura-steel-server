package com.structura.steel.gatewayservice.config;

import com.structura.steel.gatewayservice.enumeration.RoleType;
import com.structura.steel.gatewayservice.exception.AccessDeniedExceptionHandler;
import com.structura.steel.gatewayservice.exception.AuthenticationExceptionHandler;
import com.structura.steel.gatewayservice.filter.JwtClaimsConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtClaimsConverter jwtClaimsConverter;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder keyCloakJwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    @Bean
    public AuthenticationExceptionHandler authenticationExceptionHandler() {
        return new AuthenticationExceptionHandler(objectMapper);
    }

    @Bean
    public AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler(objectMapper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));

        corsConfig.setExposedHeaders(List.of("Content-Disposition"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> corsConfigurationSource())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        // Public endpoints
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/v1/core/users/first-time-password-change/**").permitAll()
                        .pathMatchers("/api/v1/core/users/forgot-password", "/api/v1/core/users/verify-otp", "/api/v1/core/users/reset-password", "/api/v1/core/users/check-email").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/users/my-profile").permitAll()

                        // Admin Only
                        .pathMatchers("/api/v1/core/users/**").hasAuthority(RoleType.ADMIN.text())

                        // Sale Order Management - GET cho mọi role, POST/PUT/DELETE cho ADMIN + SALER
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/sale/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/sale/*/details/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/sale/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/sale/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/sale/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/sale/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/sale/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/sale/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())

                        // Sale Debts - GET cho mọi role, POST/PUT/DELETE cho ADMIN + SALER + ACCOUNTANT
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/sale/*/debts/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/sale/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/sale/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/sale/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())

                        // Purchase Management - GET cho mọi role, POST/PUT/DELETE cho ADMIN + IMPORTER
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/purchase/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/purchase/*/details/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/purchase/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/purchase/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/purchase/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/purchase/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/purchase/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/purchase/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())

                        // Purchase Debts - GET cho mọi role, POST/PUT/DELETE cho ADMIN + IMPORTER + ACCOUNTANT
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/purchase/*/debts/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/purchase/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/purchase/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/purchase/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.ACCOUNTANT.text())

                        // Delivery Order Management - GET cho mọi role
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/delivery/**").authenticated()

                        // Delivery POST
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/delivery/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/delivery/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.IMPORTER.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/delivery/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.IMPORTER.text())

                        // Delivery Debts - GET cho mọi role, POST/PUT/DELETE cho tất cả roles
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/delivery/*/debts/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/delivery/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/delivery/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/delivery/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())

                        // Debt & Payment Management - ACCOUNTANT + ADMIN only
                        .pathMatchers(HttpMethod.GET, "/api/v1/core/payments/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/v1/core/payments/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.PUT, "/api/v1/core/payments/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.ACCOUNTANT.text())
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/core/payments/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.ACCOUNTANT.text())

                        // Default rule: any other request must be authenticated
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(authenticationExceptionHandler())
                        .accessDeniedHandler(accessDeniedExceptionHandler())
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtClaimsConverter))
                )
                .build();
    }
}
