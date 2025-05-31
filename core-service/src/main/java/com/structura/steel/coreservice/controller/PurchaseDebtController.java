// PurchaseDebtController.java
package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseDebtRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.coreservice.service.PurchaseDebtService;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseDebtRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase/{purchaseId}/debts")
@RequiredArgsConstructor
public class PurchaseDebtController {

    private final PurchaseDebtService purchaseDebtService;

    @GetMapping
    public ResponseEntity<PagingResponse<PurchaseDebtResponseDto>> getAllPurchaseDebts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "all", defaultValue = AppConstants.GET_ALL) boolean all,
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.getAllPurchaseDebts(pageNo, pageSize, sortBy, sortDir, all, purchaseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDebtResponseDto> getPurchaseDebtById(
            @PathVariable Long id, @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.getPurchaseDebtById(id, purchaseId));
    }

    @PostMapping
    public ResponseEntity<PurchaseDebtResponseDto> createPurchaseDebt(
            @RequestBody PurchaseDebtRequestDto dto, @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.createPurchaseDebt(dto, purchaseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDebtResponseDto> updatePurchaseDebt(
            @PathVariable Long id, @RequestBody UpdatePurchaseDebtRequestDto dto,
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.updatePurchaseDebt(id, dto, purchaseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseDebt(@PathVariable Long id, @PathVariable Long purchaseId) {
        purchaseDebtService.deletePurchaseDebtById(id, purchaseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<PurchaseDebtResponseDto>> createPurchaseDebtsBatch(
            @PathVariable Long purchaseId,
            @RequestBody List<PurchaseDebtRequestDto> batchDto) {

        return ResponseEntity.ok(purchaseDebtService.createPurchaseDebtsBatch(batchDto, purchaseId));
    }
}
