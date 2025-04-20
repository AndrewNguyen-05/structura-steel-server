package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.coreservice.service.DeliveryOrderService;
import com.structura.steel.dto.request.DeliveryOrderRequestDto;
import com.structura.steel.dto.response.DeliveryOrderResponseDto;
import com.structura.steel.dto.response.GetAllDeliveryOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @GetMapping
    public ResponseEntity<PagingResponse<GetAllDeliveryOrderResponseDto>> getAllDeliveryOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {
        return ResponseEntity.ok(deliveryOrderService.getAllDeliveryOrders(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryOrderResponseDto> getDeliveryOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryOrderService.getDeliveryOrderById(id));
    }

    @PostMapping
    public ResponseEntity<DeliveryOrderResponseDto> createDeliveryOrder(@RequestBody DeliveryOrderRequestDto dto) {
        return ResponseEntity.ok(deliveryOrderService.createDeliveryOrder(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryOrderResponseDto> updateDeliveryOrder(
            @PathVariable Long id, @RequestBody DeliveryOrderRequestDto dto) {
        return ResponseEntity.ok(deliveryOrderService.updateDeliveryOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryOrder(@PathVariable Long id) {
        deliveryOrderService.deleteDeliveryOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
