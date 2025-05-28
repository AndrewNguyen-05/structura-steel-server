package com.structura.steel.commons.client;

import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "partner-service", url = "http://localhost:8030/partner-service")
public interface PartnerFeignClient {

    @GetMapping("/partners/{id}")
    PartnerResponseDto getPartnerById(@PathVariable Long id);

    @GetMapping("/partners/{partnerId}/projects/{projectId}")
    PartnerProjectResponseDto getPartnerProject(
            @PathVariable Long partnerId,
            @PathVariable Long projectId);

    @GetMapping("/partners/batch")
    List<PartnerResponseDto> getPartnersByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/partners/{partnerId}/projects/batch")
    List<PartnerProjectResponseDto> getProjectsBatch(
            @PathVariable("partnerId") Long partnerId,
            @RequestParam("ids") List<Long> ids
    );

    @GetMapping("/partners/{partnerId}/projects/batch/ids")
    List<PartnerProjectResponseDto> getProjectsBatchByIds(
            @PathVariable("partnerId") Long partnerId,
            @RequestParam("ids") List<Long> ids
    );

    @PutMapping("/partners/{partnerId}/update-debt")
    void updatePartnerDebt(@PathVariable Long partnerId, @RequestBody UpdatePartnerDebtRequestDto dto);
}
