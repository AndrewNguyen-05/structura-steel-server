package com.structura.steel.gatewayservice.config;

import com.structura.steel.gatewayservice.exception.AccessDeniedExceptionHandler;
import com.structura.steel.gatewayservice.exception.AuthenticationExceptionHandler;
import com.structura.steel.gatewayservice.filter.JwtClaimsConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
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
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
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
