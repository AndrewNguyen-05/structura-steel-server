package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.dto.request.SaleOrderRequestDto;
import com.structura.steel.dto.response.GetAllSaleOrderResponseDto;
import com.structura.steel.dto.response.SaleOrderResponseDto;

public interface SaleOrderService {

    SaleOrderResponseDto createSaleOrder(SaleOrderRequestDto dto);

    SaleOrderResponseDto updateSaleOrder(Long id, SaleOrderRequestDto dto);

    SaleOrderResponseDto getSaleOrderById(Long id);

    void deleteSaleOrderById(Long id);

    PagingResponse<GetAllSaleOrderResponseDto> getAllSaleOrders(int pageNo, int pageSize, String sortBy, String sortDir);
}
