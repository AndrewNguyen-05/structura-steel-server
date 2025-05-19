package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.SaleOrderDetailRequestDto;
import com.structura.steel.dto.response.GetAllSaleOrderDetailResponseDto;
import com.structura.steel.dto.response.SaleOrderDetailResponseDto;

import java.util.List;

public interface SaleOrderDetailService {

    SaleOrderDetailResponseDto createSaleOrderDetail(SaleOrderDetailRequestDto dto, Long saleId);

    SaleOrderDetailResponseDto updateSaleOrderDetail(Long id, SaleOrderDetailRequestDto dto, Long saleId);

    SaleOrderDetailResponseDto getSaleOrderDetailById(Long id, Long saleId);

    void deleteSaleOrderDetailById(Long id, Long saleId);

    PagingResponse<GetAllSaleOrderDetailResponseDto> getAllSaleOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId);

    List<SaleOrderDetailResponseDto> createSaleOrderDetailsBatch(List<SaleOrderDetailRequestDto> batchDto, Long saleId);
}
