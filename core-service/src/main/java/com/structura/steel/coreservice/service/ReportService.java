package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.response.report.DailySummaryDto;
import com.structura.steel.commons.dto.core.response.report.PayableReportDto;
import com.structura.steel.commons.dto.core.response.report.ProfitLossReportDto;
import com.structura.steel.commons.dto.core.response.report.ReceivableReportDto;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    /**
     * Lấy dữ liệu cho báo cáo công nợ phải thu.
     * @return Danh sách các khoản nợ phải thu
     */
    List<ReceivableReportDto> getReceivableReportData(Instant start, Instant end);

    /**
     * Lấy dữ liệu cho báo cáo công nợ phải trả.
     * @return Danh sách các khoản nợ phải trả
     */
    List<PayableReportDto> getPayableReportData(Instant start, Instant end);

    /**
     * Lấy dữ liệu cho báo cáo lãi/lỗ theo từng đơn hàng bán.
     * @return Danh sách lợi nhuận theo từng đơn bán
     */
    List<ProfitLossReportDto> getProfitLossReportData(Instant start, Instant end);

    DailySummaryDto getDailyActivityReportData();

}