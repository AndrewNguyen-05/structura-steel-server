package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderRequestDto;
import com.structura.steel.commons.dto.core.response.sale.GetAllSaleOrderResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderResponseDto;

import java.util.List;

public interface SaleOrderService {

    SaleOrderResponseDto createSaleOrder(SaleOrderRequestDto dto);

    SaleOrderResponseDto updateSaleOrder(Long id, SaleOrderRequestDto dto);

    SaleOrderResponseDto getSaleOrderById(Long id);

    void deleteSaleOrderById(Long id);

    PagingResponse<GetAllSaleOrderResponseDto> getAllSaleOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    List<String> suggestSales(String prefix, int size);
}
