package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.PurchaseOrderDetailRequestDto;
import com.structura.steel.dto.response.PurchaseOrderDetailResponseDto;

public interface PurchaseOrderDetailService {

    PurchaseOrderDetailResponseDto createPurchaseOrderDetail(PurchaseOrderDetailRequestDto dto, Long purchaseId);

    PurchaseOrderDetailResponseDto updatePurchaseOrderDetail(Long id, PurchaseOrderDetailRequestDto dto, Long purchaseId);

    PurchaseOrderDetailResponseDto getPurchaseOrderDetailById(Long id, Long purchaseId);

    void deletePurchaseOrderDetailById(Long id, Long purchaseId);

    PagingResponse<PurchaseOrderDetailResponseDto> getAllPurchaseOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, Long purchaseId);
}
