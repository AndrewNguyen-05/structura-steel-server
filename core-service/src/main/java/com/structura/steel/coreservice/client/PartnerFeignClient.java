package com.structura.steel.coreservice.client;

import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8030/partner-service")
public interface PartnerFeignClient {

    @GetMapping("/partners/{id}")
    PartnerResponseDto getPartnerById(@PathVariable Long id);

    @GetMapping("/partners/{partnerId}/projects/{projectId}")
    PartnerProjectResponseDto getPartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId);
}
