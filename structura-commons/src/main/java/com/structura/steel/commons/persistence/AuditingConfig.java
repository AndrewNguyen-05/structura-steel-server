package com.structura.steel.commons.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(
        auditorAwareRef = "auditorProvider",
        dateTimeProviderRef = "truncatedDateTimeProvider"
)
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> {
                    if (authentication instanceof AnonymousAuthenticationToken) {
                        return "System";
                    }
                    return authentication.getName();
                });
    }

    @Bean(name = "truncatedDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        // luôn trả về Instant.now() đã truncate đến giây
        return () -> Optional.of(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
