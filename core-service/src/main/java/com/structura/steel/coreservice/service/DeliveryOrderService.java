package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.delivery.UpdateDeliveryOrderRequestDto;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.delivery.GetAllDeliveryOrderResponseDto;
import com.structura.steel.coreservice.entity.DeliveryOrder;

import java.util.List;

public interface DeliveryOrderService {

    DeliveryOrderResponseDto createDeliveryOrder(DeliveryOrderRequestDto dto);

    DeliveryOrderResponseDto updateDeliveryOrder(Long id, UpdateDeliveryOrderRequestDto dto);

    DeliveryOrderResponseDto getDeliveryOrderById(Long id);

    void deleteDeliveryOrderById(Long id);

    PagingResponse<GetAllDeliveryOrderResponseDto> getAllDeliveryOrders(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted, String searchKeyword);

    List<String> suggestDeliveries(String prefix, int size);

    DeliveryOrderResponseDto cancelDeliveryOrder(Long id, String cancellationReason);

    void checkAndUpdateDoneStatus(DeliveryOrder order);

    void softDeleteDeliveryOrder(Long id);

    DeliveryOrderResponseDto restoreDeliveryOrder(Long id);
}
