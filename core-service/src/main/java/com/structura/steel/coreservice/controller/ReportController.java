package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.response.report.DailySummaryDto;
import com.structura.steel.commons.dto.core.response.report.PayableReportDto;
import com.structura.steel.commons.dto.core.response.report.ProfitLossReportDto;
import com.structura.steel.commons.dto.core.response.report.ReceivableReportDto;
import com.structura.steel.coreservice.utils.ExcelReportGenerator;
import com.structura.steel.coreservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final ExcelReportGenerator excelGenerator;

    @GetMapping("/receivables")
    public ResponseEntity<?> getReceivableReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
            @RequestParam(required = false) boolean download) throws IOException {

        List<ReceivableReportDto> data = reportService.getReceivableReportData(start, end);

        if (download) {
            byte[] excelFile = excelGenerator.generateReceivableReport(data);
            String filename = "BaoCaoCongNoPhaiThu.xlsx";
            return createExcelResponse(excelFile, filename);
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("/payables")
    public ResponseEntity<?> getPayableReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
            @RequestParam(required = false) boolean download) throws IOException {

        List<PayableReportDto> data = reportService.getPayableReportData(start, end);

        if (download) {
            byte[] excelFile = excelGenerator.generatePayableReport(data);
            String filename = "BaoCaoCongNoPhaiTra.xlsx";
            return createExcelResponse(excelFile, filename);
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("/profit-loss")
    public ResponseEntity<?> getProfitLossReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
            @RequestParam(required = false) boolean download) throws IOException {

        List<ProfitLossReportDto> data = reportService.getProfitLossReportData(start, end);

        if (download) {
            byte[] excelFile = excelGenerator.generateProfitLossReport(data);
            String filename = "BaoCaoLaiLo.xlsx";
            return createExcelResponse(excelFile, filename);
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("/daily-activity")
    public ResponseEntity<?> downloadDailyActivityReport(
            @RequestParam(required = false) boolean download) throws IOException {

        DailySummaryDto data = reportService.getDailyActivityReportData();

        if (download) {
            byte[] excelFile = excelGenerator.generateDailyActivityReport(data);
            String filename = "BaoCaoHoatDongNgay_" + LocalDate.now() + ".xlsx";
            return createExcelResponse(excelFile, filename);
        } else {
            return ResponseEntity.ok(data);
        }
    }

    private ResponseEntity<byte[]> createExcelResponse(byte[] excelFile, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}