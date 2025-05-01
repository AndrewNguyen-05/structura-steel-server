package com.structura.steel.commons.config;

import com.structura.steel.commons.exception.exceptionHandlers.GlobalExceptionHandler;
import com.structura.steel.commons.mapper.MapperConfig;
import com.structura.steel.commons.persistence.AuditingConfig;
import com.structura.steel.commons.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Import({SecurityConfig.class, AuditingConfig.class, MapperConfig.class, GlobalExceptionHandler.class})
@Configuration
@EnableScheduling
//@EnableFeignClients(basePackages = "com.structura.steel.commons.client")
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class ApplicationConfig {

}
