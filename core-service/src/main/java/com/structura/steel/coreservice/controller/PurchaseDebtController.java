// PurchaseDebtController.java
package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.coreservice.service.PurchaseDebtService;
import com.structura.steel.dto.request.PurchaseDebtRequestDto;
import com.structura.steel.dto.response.PurchaseDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.getAllPurchaseDebts(pageNo, pageSize, sortBy, sortDir, purchaseId));
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
            @PathVariable Long id, @RequestBody PurchaseDebtRequestDto dto,
            @PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseDebtService.updatePurchaseDebt(id, dto, purchaseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseDebt(@PathVariable Long id, @PathVariable Long purchaseId) {
        purchaseDebtService.deletePurchaseDebtById(id, purchaseId);
        return ResponseEntity.noContent().build();
    }
}
