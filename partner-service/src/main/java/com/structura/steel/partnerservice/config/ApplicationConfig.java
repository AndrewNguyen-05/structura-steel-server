package com.structura.steel.partnerservice.config;

import com.structura.steel.commons.config.FeignConfig;
import com.structura.steel.commons.exception.exceptionHandlers.GlobalExceptionHandler;
import com.structura.steel.commons.mapper.MapperConfig;
import com.structura.steel.commons.persistence.AuditingConfig;
import com.structura.steel.commons.security.SecurityConfig;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerSearchRepository;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Slf4j
@Import({SecurityConfig.class, AuditingConfig.class, MapperConfig.class, GlobalExceptionHandler.class})
@Configuration
@EnableScheduling
@EnableFeignClients(
        basePackages = "com.structura.steel.commons.client",
        defaultConfiguration = FeignConfig.class)
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class ApplicationConfig {

}
