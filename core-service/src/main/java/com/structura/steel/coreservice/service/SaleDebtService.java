package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.SaleDebtRequestDto;
import com.structura.steel.dto.response.GetAllSaleDebtResponseDto;
import com.structura.steel.dto.response.SaleDebtResponseDto;

public interface SaleDebtService {

    SaleDebtResponseDto createSaleDebt(SaleDebtRequestDto dto, Long saleId);

    SaleDebtResponseDto updateSaleDebt(Long id, SaleDebtRequestDto dto, Long saleId);

    SaleDebtResponseDto getSaleDebtById(Long id, Long saleId);

    void deleteSaleDebtById(Long id, Long saleId);

    PagingResponse<GetAllSaleDebtResponseDto> getAllSaleDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId);
}
