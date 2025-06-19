package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.response.analytic.AgingReportDto;
import com.structura.steel.commons.dto.core.response.analytic.DebtStatusDistributionDto;
import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.SummaryDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;
import com.structura.steel.commons.enumeration.DebtType;
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

    //1. Overview Dashboard
    // dang the (cards) de hien thi thong tin
    @GetMapping("/summary")
    public ResponseEntity<SummaryDto> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {

        SummaryDto data = analyticService.getSummary(start, end);
        return ResponseEntity.ok(data);
    }

    //2. Phan tich kinh doanh (Business Analytic)
    //a. Doanh thu theo thgian
    // dang bieu do duong (line chart) - truc X la thgian, truc Y la tong doanh thu
    @GetMapping("/revenue-over-time")
    public ResponseEntity<List<RevenueDataPointDto>> getRevenueOverTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {

        List<RevenueDataPointDto> data = analyticService.getRevenueOverTime(start, end);
        return ResponseEntity.ok(data);
    }

    //b. Top 5 khach hang & sp
    // dang bieu do cot ngang (horizontal bar chart)
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

    //3. Phan tich cong no (debt)
    //a. ty le cac khoan no
    // bieu do tron (pie chart)
    @GetMapping("/debt-status")
    public ResponseEntity<List<DebtStatusDistributionDto>> getDebtStatusDistribution(@RequestParam DebtType type) {

        List<DebtStatusDistributionDto> data = analyticService.getDebtStatusDistribution(type);
        return ResponseEntity.ok(data);
    }

    //b. thgian no (debt aging report)
    // dang bang (table)
    @GetMapping("/aging-report")
    public ResponseEntity<List<AgingReportDto>> getAgingReport(@RequestParam DebtType type) {

        List<AgingReportDto> data = analyticService.getAgingReport(type);
        return ResponseEntity.ok(data);
    }
}
