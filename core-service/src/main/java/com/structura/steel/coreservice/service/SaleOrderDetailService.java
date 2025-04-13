package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.SaleOrderDetailRequestDto;
import com.structura.steel.dto.response.SaleOrderDetailResponseDto;

public interface SaleOrderDetailService {

    SaleOrderDetailResponseDto createSaleOrderDetail(SaleOrderDetailRequestDto dto, Long saleId);

    SaleOrderDetailResponseDto updateSaleOrderDetail(Long id, SaleOrderDetailRequestDto dto, Long saleId);

    SaleOrderDetailResponseDto getSaleOrderDetailById(Long id, Long saleId);

    void deleteSaleOrderDetailById(Long id, Long saleId);

    PagingResponse<SaleOrderDetailResponseDto> getAllSaleOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId);
}
