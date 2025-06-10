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

                        // Admin Only
                        .pathMatchers("/api/v1/core/users/**").hasAuthority(RoleType.ADMIN.text())

                        // Sale Order Management
                        .pathMatchers("/api/v1/core/sale/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers("/api/v1/core/sale/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text())
                        .pathMatchers("/api/v1/core/sale/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())


                        // Purchase Order Management
                        .pathMatchers("/api/v1/core/purchase/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers("/api/v1/core/purchase/*/details/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text())
                        .pathMatchers("/api/v1/core/purchase/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.ACCOUNTANT.text())

                        // Delivery Order Management (cả nhập và xuất)
                        .pathMatchers("/api/v1/core/delivery/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.SALER.text(), RoleType.IMPORTER.text())
                        .pathMatchers("/api/v1/core/delivery/*/debts/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.IMPORTER.text(), RoleType.SALER.text(), RoleType.ACCOUNTANT.text())

                        // Debt & Payment Management
                        .pathMatchers("/api/v1/core/payments/**").hasAnyAuthority(RoleType.ADMIN.text(), RoleType.ACCOUNTANT.text())

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
