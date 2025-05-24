package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.coreservice.service.PurchaseOrderDetailService;
import com.structura.steel.commons.dto.core.request.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.PurchaseOrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase/{purchaseId}/details")
@RequiredArgsConstructor
public class PurchaseOrderDetailController {

    private final PurchaseOrderDetailService purchaseOrderDetailService;

    @GetMapping
    public ResponseEntity<PagingResponse<PurchaseOrderDetailResponseDto>> getAllPurchaseOrderDetails(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "all", defaultValue = AppConstants.GET_ALL) boolean all,
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseOrderDetailService
                .getAllPurchaseOrderDetails(pageNo, pageSize, sortBy, sortDir, all, purchaseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDetailResponseDto> getPurchaseOrderDetailById(
            @PathVariable Long id, @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseOrderDetailService.getPurchaseOrderDetailById(id, purchaseId));
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDetailResponseDto> createPurchaseOrderDetail(
            @RequestBody PurchaseOrderDetailRequestDto dto, @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseOrderDetailService.createPurchaseOrderDetail(dto, purchaseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderDetailResponseDto> updatePurchaseOrderDetail(
            @PathVariable Long id, @RequestBody PurchaseOrderDetailRequestDto dto,
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseOrderDetailService.updatePurchaseOrderDetail(id, dto, purchaseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderDetail(@PathVariable Long id, @PathVariable Long purchaseId) {
        purchaseOrderDetailService.deletePurchaseOrderDetailById(id, purchaseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<PurchaseOrderDetailResponseDto>> createPurchaseDetailsBatch(
            @PathVariable Long purchaseId,
            @RequestBody List<PurchaseOrderDetailRequestDto> batchDto) {

        return ResponseEntity.ok(purchaseOrderDetailService.createPurchaseOrderDetailsBatch(batchDto, purchaseId));
    }
}
