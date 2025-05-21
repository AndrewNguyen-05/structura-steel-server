package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.PurchaseOrderRequestDto;
import com.structura.steel.dto.response.GetAllPurchaseOrderResponseDto;
import com.structura.steel.dto.response.PurchaseOrderResponseDto;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto getPurchaseOrderById(Long id);

    void deletePurchaseOrderById(Long id);

    PagingResponse<GetAllPurchaseOrderResponseDto> getAllPurchaseOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    List<String> suggestPurchases(String prefix, int size);
}