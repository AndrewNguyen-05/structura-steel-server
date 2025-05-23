package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.dto.core.request.SaleOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllSaleOrderDetailResponseDto;
import com.structura.steel.commons.dto.core.response.SaleOrderDetailResponseDto;
import com.structura.steel.coreservice.service.SaleOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale/{saleId}/details")
@RequiredArgsConstructor
public class SaleOrderDetailController {

    private final SaleOrderDetailService saleOrderDetailService;

    @GetMapping
    public ResponseEntity<PagingResponse<GetAllSaleOrderDetailResponseDto>> getAllSaleOrderDetails(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long saleId) {
        return ResponseEntity.ok(saleOrderDetailService.getAllSaleOrderDetails(pageNo, pageSize, sortBy, sortDir, saleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderDetailResponseDto> getSaleOrderDetailById(@PathVariable Long id, @PathVariable Long saleId) {
        return ResponseEntity.ok(saleOrderDetailService.getSaleOrderDetailById(id, saleId));
    }

    @PostMapping
    public ResponseEntity<SaleOrderDetailResponseDto> createSaleOrderDetail(@RequestBody SaleOrderDetailRequestDto saleOrderDetailRequestDto, @PathVariable Long saleId) {
        return ResponseEntity.ok(saleOrderDetailService.createSaleOrderDetail(saleOrderDetailRequestDto, saleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleOrderDetailResponseDto> updateSaleOrderDetail(@PathVariable Long id,
                                                                            @RequestBody SaleOrderDetailRequestDto saleOrderDetailRequestDto,
                                                                            @PathVariable Long saleId) {
        return ResponseEntity.ok(saleOrderDetailService.updateSaleOrderDetail(id, saleOrderDetailRequestDto, saleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleOrderDetail(@PathVariable Long id, @PathVariable Long saleId) {
        saleOrderDetailService.deleteSaleOrderDetailById(id, saleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<SaleOrderDetailResponseDto>> createDetailsBatch(@PathVariable Long saleId,
                                                                        @RequestBody List<SaleOrderDetailRequestDto> batchDto) {
        return ResponseEntity.ok(saleOrderDetailService.createSaleOrderDetailsBatch(batchDto, saleId));
    }
}
