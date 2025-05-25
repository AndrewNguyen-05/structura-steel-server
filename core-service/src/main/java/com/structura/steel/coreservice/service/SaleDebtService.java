package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.SaleDebtRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllSaleDebtResponseDto;
import com.structura.steel.commons.dto.core.response.SaleDebtResponseDto;

import java.util.List;

public interface SaleDebtService {

    SaleDebtResponseDto createSaleDebt(SaleDebtRequestDto dto, Long saleId);

    SaleDebtResponseDto updateSaleDebt(Long id, SaleDebtRequestDto dto, Long saleId);

    SaleDebtResponseDto getSaleDebtById(Long id, Long saleId);

    void deleteSaleDebtById(Long id, Long saleId);

    PagingResponse<SaleDebtResponseDto> getAllSaleDebts(int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long saleId);

    List<SaleDebtResponseDto> createSaleDebtsBatch(List<SaleDebtRequestDto> batchDto, Long saleId);
}
