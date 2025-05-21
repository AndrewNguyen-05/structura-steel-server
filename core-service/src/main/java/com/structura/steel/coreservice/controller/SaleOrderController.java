package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.dto.request.SaleOrderRequestDto;
import com.structura.steel.dto.response.GetAllSaleOrderResponseDto;
import com.structura.steel.dto.response.SaleOrderResponseDto;
import com.structura.steel.coreservice.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleOrderController {

    private final SaleOrderService saleOrderService;

    @GetMapping
    public ResponseEntity<PagingResponse<GetAllSaleOrderResponseDto>> getAllSaleOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(saleOrderService.getAllSaleOrders(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam("prefix") String prefix,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(saleOrderService.suggestSales(prefix, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderResponseDto> getSaleOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(saleOrderService.getSaleOrderById(id));
    }

    @PostMapping
    public ResponseEntity<SaleOrderResponseDto> createSaleOrder(@RequestBody SaleOrderRequestDto saleOrderRequestDto) {
        return ResponseEntity.ok(saleOrderService.createSaleOrder(saleOrderRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleOrderResponseDto> updateSaleOrder(@PathVariable Long id,
                                                                @RequestBody SaleOrderRequestDto saleOrderRequestDto) {
        return ResponseEntity.ok(saleOrderService.updateSaleOrder(id, saleOrderRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleOrder(@PathVariable Long id) {
        saleOrderService.deleteSaleOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
