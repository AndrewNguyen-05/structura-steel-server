package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.response.analytic.AgingReportDto;
import com.structura.steel.commons.dto.core.response.analytic.DebtStatusDistributionDto;
import com.structura.steel.commons.dto.core.response.analytic.RevenueDataPointDto;
import com.structura.steel.commons.dto.core.response.analytic.SummaryDto;
import com.structura.steel.commons.dto.core.response.analytic.TopItemDto;
import com.structura.steel.commons.enumeration.DebtType;

import java.time.Instant;
import java.util.List;

public interface AnalyticService {

    SummaryDto getSummary(Instant start, Instant end);

    List<RevenueDataPointDto> getRevenueOverTime(Instant start, Instant end);

    List<TopItemDto> getTopCustomers(Instant start, Instant end, int limit);

    List<TopItemDto> getTopProducts(Instant start, Instant end, int limit);

    List<DebtStatusDistributionDto> getDebtStatusDistribution(DebtType type);

    List<AgingReportDto> getAgingReport(DebtType type);
}
