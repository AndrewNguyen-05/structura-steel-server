package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.dto.request.SaleDebtRequestDto;
import com.structura.steel.dto.response.GetAllSaleDebtResponseDto;
import com.structura.steel.dto.response.SaleDebtResponseDto;
import com.structura.steel.coreservice.service.SaleDebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale/{saleId}/debts")
@RequiredArgsConstructor
public class SaleDebtController {

    private final SaleDebtService saleDebtService;

    @GetMapping
    public ResponseEntity<PagingResponse<GetAllSaleDebtResponseDto>> getAllSaleDebts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long saleId) {
        return ResponseEntity.ok(saleDebtService.getAllSaleDebts(pageNo, pageSize, sortBy, sortDir, saleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDebtResponseDto> getSaleDebtById(@PathVariable Long id, @PathVariable Long saleId) {
        return ResponseEntity.ok(saleDebtService.getSaleDebtById(id, saleId));
    }

    @PostMapping
    public ResponseEntity<SaleDebtResponseDto> createSaleDebt(@RequestBody SaleDebtRequestDto saleDebtRequestDto, @PathVariable Long saleId) {
        return ResponseEntity.ok(saleDebtService.createSaleDebt(saleDebtRequestDto, saleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDebtResponseDto> updateSaleDebt(@PathVariable Long id,
                                                              @RequestBody SaleDebtRequestDto saleDebtRequestDto,
                                                              @PathVariable Long saleId) {
        return ResponseEntity.ok(saleDebtService.updateSaleDebt(id, saleDebtRequestDto, saleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleDebt(@PathVariable Long id, @PathVariable Long saleId) {
        saleDebtService.deleteSaleDebtById(id, saleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<SaleDebtResponseDto>> createSaleDebtsBatch(
            @PathVariable Long saleId,
            @RequestBody List<SaleDebtRequestDto> batchDto) {

        return ResponseEntity.ok(saleDebtService.createSaleDebtsBatch(batchDto, saleId));
    }
}
