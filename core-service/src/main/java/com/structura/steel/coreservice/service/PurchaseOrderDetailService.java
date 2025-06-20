package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderDetailRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderDetailResponseDto;

import java.util.List;

public interface PurchaseOrderDetailService {

    PurchaseOrderDetailResponseDto createPurchaseOrderDetail(PurchaseOrderDetailRequestDto dto, Long purchaseId);

    PurchaseOrderDetailResponseDto updatePurchaseOrderDetail(Long id, UpdatePurchaseOrderDetailRequestDto dto, Long purchaseId);

    PurchaseOrderDetailResponseDto getPurchaseOrderDetailById(Long id, Long purchaseId);

    void deletePurchaseOrderDetailById(Long id, Long purchaseId);

    PagingResponse<PurchaseOrderDetailResponseDto> getAllPurchaseOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long purchaseId);

    List<PurchaseOrderDetailResponseDto> createPurchaseOrderDetailsBatch(List<PurchaseOrderDetailRequestDto> batchDto,
                                                                         Long purchaseId);
}
