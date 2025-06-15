package com.structura.steel.coreservice.utils;

import com.structura.steel.commons.dto.core.response.report.DailySummaryDto;
import com.structura.steel.commons.dto.core.response.report.PayableReportDto;
import com.structura.steel.commons.dto.core.response.report.ProfitLossReportDto;
import com.structura.steel.commons.dto.core.response.report.ReceivableReportDto;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.DebtType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Component
public class ExcelReportGenerator {

    // === CÁC PHƯƠNG THỨC HELPER ===
    /**
     * Helper method để tạo một bộ style tái sử dụng cho toàn bộ workbook.
     * Đây là "best practice" để tránh lỗi styles.xml.
     */
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private Map<String, CellStyle> createStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();
        DataFormat dataFormat = workbook.createDataFormat();

        // --- Font Definitions ---
        Font companyFont = workbook.createFont();
        companyFont.setBold(true);
        companyFont.setFontName("Times New Roman");
        companyFont.setFontHeightInPoints((short) 11);

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setFontName("Times New Roman");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setFontName("Times New Roman");

        Font defaultFont = workbook.createFont();
        defaultFont.setFontName("Times New Roman");
        defaultFont.setFontHeightInPoints((short) 11);

        Font footerFont = workbook.createFont();
        footerFont.setItalic(true);
        footerFont.setFontName("Times New Roman");
        footerFont.setFontHeightInPoints((short) 10);

        // --- Style Definitions ---
        CellStyle companyStyle = workbook.createCellStyle();
        companyStyle.setFont(companyFont);
        companyStyle.setAlignment(HorizontalAlignment.CENTER);
        styles.put("company", companyStyle);

        CellStyle departmentStyle = workbook.createCellStyle();
        departmentStyle.setFont(defaultFont);
        departmentStyle.setAlignment(HorizontalAlignment.CENTER);
        styles.put("department", departmentStyle);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("title", titleStyle);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        byte[] rgb = new byte[]{(byte) 204, (byte) 255, (byte) 255};
        XSSFColor customColor = new XSSFColor(rgb, null);
        ((org.apache.poi.xssf.usermodel.XSSFCellStyle) headerStyle).setFillForegroundColor(customColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        styles.put("header", headerStyle);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(defaultFont);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        styles.put("cell", cellStyle);

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(cellStyle);
        currencyStyle.setDataFormat(dataFormat.getFormat("#,##0\" VNĐ\""));
        styles.put("currency", currencyStyle);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(cellStyle);
        dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));
        styles.put("date", dateStyle);

        CellStyle dateTimeStyle = workbook.createCellStyle();
        dateTimeStyle.cloneStyleFrom(cellStyle);
        dateTimeStyle.setDataFormat(dataFormat.getFormat("hh:mm:ss dd-MM-yyyy"));
        styles.put("datetime", dateTimeStyle);

        // ***SỬA LỖI: Thêm style cho footer***
        CellStyle footerStyle = workbook.createCellStyle();
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.CENTER);
        styles.put("footer", footerStyle);

        return styles;
    }

    private void createHeaderSection(Sheet sheet, Map<String, CellStyle> styles, String reportTitle, int mergeUntilCol) {
        Row companyRow = sheet.createRow(0);
        companyRow.createCell(0).setCellValue("CÔNG TY TNHH THÉP STRUCTURA");
        companyRow.getCell(0).setCellStyle(styles.get("company"));

        Row departmentRow = sheet.createRow(1);
        departmentRow.createCell(0).setCellValue("PHÒNG BAN KẾ TOÁN");
        departmentRow.getCell(0).setCellStyle(styles.get("department"));

        //        // Thêm địa chỉ
        //        Row addressRow = sheet.createRow(2);
        //        addressRow.createCell(0).setCellValue("Địa chỉ: 123 đường ABC, phường XYZ, Tp. Thủ Dầu Một, Bình Dương");
        //        addressRow.getCell(0).setCellStyle(styles.get("department"));

        Row titleRow = sheet.createRow(3); // Cách 1 dòng
        titleRow.setHeightInPoints(25);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(reportTitle.toUpperCase());
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, mergeUntilCol));
    }

    private void createFooterSection(Sheet sheet, Map<String, CellStyle> styles, int startRowNum, int mergeUntilCol) {
        Row addressRow = sheet.createRow(startRowNum);
        Cell addressCell = addressRow.createCell(0);
        addressCell.setCellValue("Địa chỉ: 123 Đường ABC, Phường XYZ, TP. Thủ Dầu Một, Bình Dương");
        addressCell.setCellStyle(styles.get("footer"));
        sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, mergeUntilCol));
    }

    // === CÁC PHƯƠNG THỨC TẠO BÁO CÁO ===

    public byte[] generateReceivableReport(List<ReceivableReportDto> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Map<String, CellStyle> styles = createStyles(workbook);
            Sheet sheet = workbook.createSheet("CongNoPhaiThu");

            String[] headers = {"TÊN KHÁCH HÀNG", "MÃ ĐƠN BÁN", "NGÀY NỢ", "SỐ TIỀN CÒN LẠI", "TRẠNG THÁI"};
            createHeaderSection(sheet, styles, "BÁO CÁO TỔNG HỢP CÔNG NỢ PHẢI THU", headers.length - 1);

            Row headerRow = sheet.createRow(5);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styles.get("header"));
            }

            int rowNum = 6;
            for (ReceivableReportDto dto : data) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(20);
                // Áp dụng style cho tất cả các ô để đảm bảo tính nhất quán
                Cell nameCell = row.createCell(0);
                nameCell.setCellValue(dto.customerName());
                nameCell.setCellStyle(styles.get("cell"));

                Cell codeCell = row.createCell(1);
                codeCell.setCellValue(dto.orderCode());
                codeCell.setCellStyle(styles.get("cell"));

                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(Date.from(dto.debtDate()));
                dateCell.setCellStyle(styles.get("date"));

                Cell amountCell = row.createCell(3);
                amountCell.setCellValue(dto.remainingAmount().doubleValue());
                amountCell.setCellStyle(styles.get("currency"));

                Cell statusCell = row.createCell(4);
                statusCell.setCellValue(toVietnamese(dto.status()));
                statusCell.setCellStyle(styles.get("cell"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generatePayableReport(List<PayableReportDto> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Map<String, CellStyle> styles = createStyles(workbook);
            Sheet sheet = workbook.createSheet("CongNoPhaiTra");

            String[] headers = {"TÊN ĐỐI TÁC", "LOẠI NỢ", "MÃ ĐƠN", "NGÀY NỢ", "SỐ TIỀN CÒN LẠI"};
            createHeaderSection(sheet, styles, "BÁO CÁO TỔNG HỢP CÔNG NỢ PHẢI TRẢ", headers.length - 1);

            Row headerRow = sheet.createRow(5);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styles.get("header"));
            }

            int rowNum = 6;
            for (PayableReportDto dto : data) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(20);

                Cell partnerCell = row.createCell(0);
                partnerCell.setCellValue(dto.partnerName());
                partnerCell.setCellStyle(styles.get("cell"));

                Cell typeCell = row.createCell(1);
                typeCell.setCellValue(toVietnamese(dto.debtType()));
                typeCell.setCellStyle(styles.get("cell"));

                Cell refCell = row.createCell(2);
                refCell.setCellValue(dto.referenceCode());
                refCell.setCellStyle(styles.get("cell"));

                Cell dateCell = row.createCell(3);
                dateCell.setCellValue(Date.from(dto.debtDate()));
                dateCell.setCellStyle(styles.get("date"));

                Cell amountCell = row.createCell(4);
                amountCell.setCellValue(dto.remainingAmount().doubleValue());
                amountCell.setCellStyle(styles.get("currency"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generateProfitLossReport(List<ProfitLossReportDto> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Map<String, CellStyle> styles = createStyles(workbook);
            Sheet sheet = workbook.createSheet("LaiLoTheoDonHang");

            String[] headers = {"MÃ ĐƠN BÁN", "KHÁCH HÀNG", "NGÀY HOÀN THÀNH", "DOANH THU", "GIÁ VỐN", "PHÍ VẬN CHUYỂN", "LỢI NHUẬN GỘP"};
            createHeaderSection(sheet, styles, "BÁO CÁO LÃI LỖ THEO ĐƠN HÀNG", headers.length - 1);

            Row headerRow = sheet.createRow(5);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styles.get("header"));
            }

            int rowNum = 6;
            for (ProfitLossReportDto dto : data) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(20);

                row.createCell(0).setCellValue(dto.saleOrderCode());
                row.getCell(0).setCellStyle(styles.get("cell"));

                row.createCell(1).setCellValue(dto.customerName());
                row.getCell(1).setCellStyle(styles.get("cell"));

                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(Date.from(dto.completionDate()));
                dateCell.setCellStyle(styles.get("date"));

                Cell revenueCell = row.createCell(3);
                revenueCell.setCellValue(dto.revenue().doubleValue());
                revenueCell.setCellStyle(styles.get("currency"));

                Cell costCell = row.createCell(4);
                costCell.setCellValue(dto.costOfGoods().doubleValue());
                costCell.setCellStyle(styles.get("currency"));

                Cell deliveryCell = row.createCell(5);
                deliveryCell.setCellValue(dto.deliveryCost().doubleValue());
                deliveryCell.setCellStyle(styles.get("currency"));

                Cell profitCell = row.createCell(6);
                profitCell.setCellValue(dto.grossProfit().doubleValue());
                profitCell.setCellStyle(styles.get("currency"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generateDailyActivityReport(DailySummaryDto data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Map<String, CellStyle> styles = createStyles(workbook);

            // Gọi các hàm con để tạo từng sheet
            createSummarySheet(workbook, styles, data.summary());
            createNewOrdersSheet(workbook, styles, data.newOrders());
            createCompletedDeliveriesSheet(workbook, styles, data.completedDeliveries());
            createPaymentTransactionsSheet(workbook, styles, data.paymentTransactions());

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createSummarySheet(Workbook workbook, Map<String, CellStyle> styles, DailySummaryDto.SummarySection summary) {
        Sheet sheet = workbook.createSheet("Tóm tắt trong ngày");
        int currentRowNum = 0;

        createHeaderSection(sheet, styles, "BÁO CÁO HOẠT ĐỘNG TRONG NGÀY", 5);
        currentRowNum = 5;

        // --- Phần tóm tắt ---
        // Gọi hàm createSummaryRow đã được cải tiến
        createSummaryRow(sheet, styles, currentRowNum++, "Số đơn bán hàng mới:", summary.newSaleOrdersCount());
        createSummaryRow(sheet, styles, currentRowNum++, "Tổng giá trị đơn bán mới:", summary.newSaleOrdersValue());
        createSummaryRow(sheet, styles, currentRowNum++, "Số đơn mua hàng mới:", summary.newPurchaseOrdersCount());
        createSummaryRow(sheet, styles, currentRowNum++, "Số chuyến giao hàng hoàn tất:", summary.completedDeliveriesCount());
        createSummaryRow(sheet, styles, currentRowNum++, "Tổng tiền đã thu:", summary.totalAmountReceived());
        createSummaryRow(sheet, styles, currentRowNum++, "Tổng tiền đã chi:", summary.totalAmountPaid());

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createNewOrdersSheet(Workbook workbook, Map<String, CellStyle> styles, List<DailySummaryDto.NewOrder> data) {
        Sheet sheet = workbook.createSheet("Đơn hàng mới");
        String[] headers = {"Thời Gian Tạo", "Loại Đơn", "Mã Đơn", "Đối Tác", "Giá Trị", "Người Tạo"};

        createHeaderSection(sheet, styles, "CHI TIẾT CÁC ĐƠN HÀNG MỚI TẠO", headers.length - 1);

        Row headerRow = sheet.createRow(5);
        headerRow.setHeightInPoints(22);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(styles.get("header"));
        }

        int rowNum = 6;
        for (DailySummaryDto.NewOrder dto : data) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(18);
            mapNewOrderToRow(row, dto, styles);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private <T> int createTableSection(Sheet sheet, Map<String, CellStyle> styles, int startRowNum, String title, String[] headers, List<T> data, RowMapper<T> mapper) {
        Row titleRow = sheet.createRow(startRowNum);
        titleRow.setHeightInPoints(22);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, headers.length - 1));

        Row headerRow = sheet.createRow(startRowNum + 1);
        headerRow.setHeightInPoints(22);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(styles.get("header"));
        }

        int rowNum = startRowNum + 2;
        for (T dto : data) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(18);
            mapper.map(row, dto, styles);
        }
        return rowNum;
    }

    private void createCompletedDeliveriesSheet(Workbook workbook, Map<String, CellStyle> styles, List<DailySummaryDto.CompletedDelivery> data) {
        Sheet sheet = workbook.createSheet("Giao hàng hoàn tất");
        String[] headers = {"Mã Giao Hàng", "Mã Đơn Gốc", "Đối Tác Nhận", "Địa Chỉ Giao", "Thời Gian Hoàn Tất"};

        createHeaderSection(sheet, styles, "CHI TIẾT GIAO NHẬN HOÀN TẤT", headers.length - 1);

        Row headerRow = sheet.createRow(5);
        headerRow.setHeightInPoints(22);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i].toUpperCase());
            cell.setCellStyle(styles.get("header"));
        }

        int rowNum = 6;
        for (DailySummaryDto.CompletedDelivery dto : data) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(18);
            mapCompletedDeliveryToRow(row, dto, styles);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createPaymentTransactionsSheet(Workbook workbook, Map<String, CellStyle> styles, List<DailySummaryDto.PaymentTransaction> data) {
        Sheet sheet = workbook.createSheet("Giao dịch thanh toán");
        String[] headers = {"Thời Gian", "Loại Giao Dịch", "Đối Tác", "Số Tiền", "Phương Thức", "Ghi Chú"};

        createHeaderSection(sheet, styles, "CHI TIẾT CÁC GIAO DỊCH THANH TOÁN", headers.length - 1);

        Row headerRow = sheet.createRow(5);
        headerRow.setHeightInPoints(22);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i].toUpperCase());
            cell.setCellStyle(styles.get("header"));
        }

        int rowNum = 6;
        for (DailySummaryDto.PaymentTransaction dto : data) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(18);
            mapPaymentTransactionToRow(row, dto, styles);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @FunctionalInterface
    interface RowMapper<T> {
        void map(Row row, T data, Map<String, CellStyle> styles);
    }

    private void createSummaryRow(Sheet sheet, Map<String, CellStyle> styles, int rowNum, String label, Object value) {
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(18);

        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(styles.get("cell")); // Áp dụng style mặc định

        Cell valueCell = row.createCell(1);
        if (value instanceof String) { // Dành cho các giá trị đã format tiền tệ
            valueCell.setCellValue((String) value);
            valueCell.setCellStyle(styles.get("cell"));
        } else if (value instanceof Number) { // Dành cho các giá trị số (count)
            valueCell.setCellValue(((Number) value).doubleValue());
            valueCell.setCellStyle(styles.get("cell")); // Sử dụng style số nếu cần
        }
    }

    private void mapNewOrderToRow(Row row, DailySummaryDto.NewOrder dto, Map<String, CellStyle> styles) {
        Cell dateCell = row.createCell(0);
        dateCell.setCellValue(Date.from(dto.createdAt()));
        dateCell.setCellStyle(styles.get("date"));

        row.createCell(1).setCellValue(dto.orderType().text());
        row.getCell(1).setCellStyle(styles.get("cell"));

        row.createCell(2).setCellValue(dto.orderCode());
        row.getCell(2).setCellStyle(styles.get("cell"));

        row.createCell(3).setCellValue(dto.partnerName());
        row.getCell(3).setCellStyle(styles.get("cell"));

        Cell valueCell = row.createCell(4);
        valueCell.setCellValue(dto.value().doubleValue());
        valueCell.setCellStyle(styles.get("currency"));

        row.createCell(5).setCellValue(dto.createdBy());
        row.getCell(5).setCellStyle(styles.get("cell"));
    }

    private void mapCompletedDeliveryToRow(Row row, DailySummaryDto.CompletedDelivery dto, Map<String, CellStyle> styles) {
        row.createCell(0).setCellValue(dto.deliveryCode());
        row.getCell(0).setCellStyle(styles.get("cell"));

        row.createCell(1).setCellValue(dto.originalOrderCode());
        row.getCell(1).setCellStyle(styles.get("cell"));

        row.createCell(2).setCellValue(dto.customerName());
        row.getCell(2).setCellStyle(styles.get("cell"));

        row.createCell(3).setCellValue(dto.deliveryAddress());
        row.getCell(3).setCellStyle(styles.get("cell"));

        Cell dateCell = row.createCell(4);
        dateCell.setCellValue(Date.from(dto.completionTime()));
        dateCell.setCellStyle(styles.get("date"));
    }

    private void mapPaymentTransactionToRow(Row row, DailySummaryDto.PaymentTransaction dto, Map<String, CellStyle> styles) {
        Cell dateCell = row.createCell(0);
        dateCell.setCellValue(Date.from(dto.paymentTime()));
        dateCell.setCellStyle(styles.get("date"));

        row.createCell(1).setCellValue(dto.transactionType().text());
        row.getCell(1).setCellStyle(styles.get("cell"));

        row.createCell(2).setCellValue(dto.partnerName());
        row.getCell(2).setCellStyle(styles.get("cell"));

        Cell amountCell = row.createCell(3);
        amountCell.setCellValue(dto.amount().doubleValue());
        amountCell.setCellStyle(styles.get("currency"));

        row.createCell(4).setCellValue(dto.paymentMethod());
        row.getCell(4).setCellStyle(styles.get("cell"));

        row.createCell(5).setCellValue(dto.notes());
        row.getCell(5).setCellStyle(styles.get("cell"));
    }

    private String toVietnamese(DebtStatus status) {
        if (status == null) return "";
        return switch (status) {
            case UNPAID -> "Chưa thanh toán";
            case PARTIALLY_PAID -> "Thanh toán một phần";
            case PAID -> "Đã thanh toán";
            case CANCELLED -> "Đã hủy";
            default -> status.name();
        };
    }

    private String toVietnamese(DebtType type) {
        if (type == null) return "";
        return switch (type) {
            case PURCHASE_DEBT -> "Nợ mua hàng";
            case DELIVERY_DEBT -> "Nợ vận chuyển";
            case SALE_DEBT -> "Nợ bán hàng";
            default -> type.name();
        };
    }
}