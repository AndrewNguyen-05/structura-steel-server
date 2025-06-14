package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryDebtRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryDebtResponseDto;

import java.util.List;

public interface DeliveryDebtService {

    DeliveryDebtResponseDto createDeliveryDebt(DeliveryDebtRequestDto dto, Long deliveryId);

    DeliveryDebtResponseDto updateDeliveryDebt(Long id, DeliveryDebtRequestDto dto, Long deliveryId);

    DeliveryDebtResponseDto getDeliveryDebtById(Long id, Long deliveryId);

    void deleteDeliveryDebtById(Long id, Long deliveryId);

    PagingResponse<DeliveryDebtResponseDto> getAllDeliveryDebts(int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long deliveryId);

    List<DeliveryDebtResponseDto> createDeliveryDebtsBatch(List<DeliveryDebtRequestDto> batchDto, Long deliveryId);
}
