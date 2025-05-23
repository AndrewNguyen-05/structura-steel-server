package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.GetAllDeliveryOrderResponseDto;

import java.util.List;

public interface DeliveryOrderService {

    DeliveryOrderResponseDto createDeliveryOrder(DeliveryOrderRequestDto dto);

    DeliveryOrderResponseDto updateDeliveryOrder(Long id, DeliveryOrderRequestDto dto);

    DeliveryOrderResponseDto getDeliveryOrderById(Long id);

    void deleteDeliveryOrderById(Long id);

    PagingResponse<GetAllDeliveryOrderResponseDto> getAllDeliveryOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    List<String> suggestDeliveries(String prefix, int size);
}
