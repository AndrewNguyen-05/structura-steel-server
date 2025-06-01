package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.coreservice.service.DeliveryDebtService;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryDebtRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/{deliveryId}/debts")
@RequiredArgsConstructor
public class DeliveryDebtController {

    private final DeliveryDebtService deliveryDebtService;

    @GetMapping
    public ResponseEntity<PagingResponse<DeliveryDebtResponseDto>> getAllDeliveryDebts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "all", defaultValue = AppConstants.GET_ALL) boolean all,
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryDebtService.getAllDeliveryDebts(pageNo, pageSize, sortBy, sortDir, all, deliveryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDebtResponseDto> getDeliveryDebtById(
            @PathVariable Long id, @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryDebtService.getDeliveryDebtById(id, deliveryId));
    }

    @PostMapping
    public ResponseEntity<DeliveryDebtResponseDto> createDeliveryDebt(
            @RequestBody DeliveryDebtRequestDto dto, @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryDebtService.createDeliveryDebt(dto, deliveryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDebtResponseDto> updateDeliveryDebt(
            @PathVariable Long id, @RequestBody DeliveryDebtRequestDto dto,
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryDebtService.updateDeliveryDebt(id, dto, deliveryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryDebt(@PathVariable Long id, @PathVariable Long deliveryId) {
        deliveryDebtService.deleteDeliveryDebtById(id, deliveryId);
        return ResponseEntity.noContent().build();
    }
}
