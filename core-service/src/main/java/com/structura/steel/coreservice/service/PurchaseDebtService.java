package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.PurchaseDebtRequestDto;
import com.structura.steel.commons.dto.core.response.PurchaseDebtResponseDto;

import java.util.List;

public interface PurchaseDebtService {

    PurchaseDebtResponseDto createPurchaseDebt(PurchaseDebtRequestDto dto, Long purchaseId);

    PurchaseDebtResponseDto updatePurchaseDebt(Long id, PurchaseDebtRequestDto dto, Long purchaseId);

    PurchaseDebtResponseDto getPurchaseDebtById(Long id, Long purchaseId);

    void deletePurchaseDebtById(Long id, Long purchaseId);

    PagingResponse<PurchaseDebtResponseDto> getAllPurchaseDebts(int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long purchaseId);

    List<PurchaseDebtResponseDto> createPurchaseDebtsBatch(List<PurchaseDebtRequestDto> batchDto, Long purchaseId);
}
