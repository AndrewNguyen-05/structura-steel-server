package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;
import com.structura.steel.coreservice.entity.analytic.RevenueOverTimeProjection;
import com.structura.steel.coreservice.repository.SaleOrderDetailRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnalyticServiceImpl implements AnalyticService {

    private final SaleOrderRepository saleOrderRepository;
    private final SaleOrderDetailRepository saleOrderDetailRepository;

    @Transactional(readOnly = true)
    public List<RevenueDataPointDto> getRevenueOverTime(Instant start, Instant end) {
        List<RevenueOverTimeProjection> projections = saleOrderRepository.findRevenueOverTime(start, end);

        return projections.stream()
                .map(p -> new RevenueDataPointDto(p.getDateLabel(), p.getTotalRevenue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopItemDto> getTopCustomers(Instant start, Instant end, int limit) {
        return saleOrderRepository.findTopCustomers(start, end, limit).stream()
                .map(p -> new TopItemDto(p.getName(), p.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopItemDto> getTopProducts(Instant start, Instant end, int limit) {
        return saleOrderDetailRepository.findTopProducts(start, end, limit).stream()
                .map(p -> new TopItemDto(p.getName(), p.getValue()))
                .collect(Collectors.toList());
    }

}
