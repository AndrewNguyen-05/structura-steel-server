package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;
import com.structura.steel.coreservice.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticController {

    private final AnalyticService analyticService;

    @GetMapping("/revenue-over-time")
    public ResponseEntity<List<RevenueDataPointDto>> getRevenueOverTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {

        List<RevenueDataPointDto> data = analyticService.getRevenueOverTime(start, end);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopItemDto>> getTopCustomers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
            @RequestParam(defaultValue = "5") int limit) {

        List<TopItemDto> data = analyticService.getTopCustomers(start, end, limit);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopItemDto>> getTopProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
            @RequestParam(defaultValue = "5") int limit) {

        List<TopItemDto> data = analyticService.getTopProducts(start, end, limit);
        return ResponseEntity.ok(data);
    }
}
