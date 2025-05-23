package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.dto.core.request.DeliveryDebtRequestDto;
import com.structura.steel.commons.dto.core.response.DeliveryDebtResponseDto;

public interface DeliveryDebtService {

    DeliveryDebtResponseDto createDeliveryDebt(DeliveryDebtRequestDto dto, Long deliveryId);

    DeliveryDebtResponseDto updateDeliveryDebt(Long id, DeliveryDebtRequestDto dto, Long deliveryId);

    DeliveryDebtResponseDto getDeliveryDebtById(Long id, Long deliveryId);

    void deleteDeliveryDebtById(Long id, Long deliveryId);

    PagingResponse<DeliveryDebtResponseDto> getAllDeliveryDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long deliveryId);
}
