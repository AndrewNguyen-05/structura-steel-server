package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;

import java.time.Instant;
import java.util.List;

public interface AnalyticService {

    List<RevenueDataPointDto> getRevenueOverTime(Instant start, Instant end);

    List<TopItemDto> getTopCustomers(Instant start, Instant end, int limit);

    List<TopItemDto> getTopProducts(Instant start, Instant end, int limit);
}
