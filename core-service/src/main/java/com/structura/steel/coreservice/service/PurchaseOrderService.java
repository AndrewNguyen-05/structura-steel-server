package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.GetAllPurchaseOrderResponseDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderResponseDto;
import com.structura.steel.coreservice.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto updatePurchaseOrder(Long id, UpdatePurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto getPurchaseOrderById(Long id);

    void deletePurchaseOrderById(Long id);

    PagingResponse<GetAllPurchaseOrderResponseDto> getAllPurchaseOrders(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted,  String searchKeyword);

    List<String> suggestPurchases(String prefix, int size);

    PurchaseOrderResponseDto cancelPurchaseOrder(Long id, String cancellationReason);

    void checkAndUpdateDoneStatus(PurchaseOrder po);

    void softDeletePurchaseOrder(Long id);

    PurchaseOrderResponseDto restorePurchaseOrder(Long id);
}