package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.DeliveryDebt;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.coreservice.mapper.DeliveryDebtMapper;
import com.structura.steel.coreservice.repository.DeliveryDebtRepository;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
import com.structura.steel.coreservice.service.DeliveryDebtService;
import com.structura.steel.dto.request.DeliveryDebtRequestDto;
import com.structura.steel.dto.response.DeliveryDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryDebtServiceImpl implements DeliveryDebtService {

    private final DeliveryDebtRepository deliveryDebtRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryDebtMapper deliveryDebtMapper;

    @Override
    public DeliveryDebtResponseDto createDeliveryDebt(DeliveryDebtRequestDto dto, Long deliveryId) {
        deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        DeliveryDebt debt = deliveryDebtMapper.toDeliveryDebt(dto);
        DeliveryDebt saved = deliveryDebtRepository.save(debt);
        return deliveryDebtMapper.toDeliveryDebtResponseDto(saved);
    }

    @Override
    public DeliveryDebtResponseDto updateDeliveryDebt(Long id, DeliveryDebtRequestDto dto, Long deliveryId) {
        DeliveryDebt existing = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));

        DeliveryOrder order = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        if (!order.getDeliveryDebts().contains(existing)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "delivery order", "id", deliveryId);
        }

        deliveryDebtMapper.updateDeliveryDebtFromDto(dto, existing);
        DeliveryDebt updated = deliveryDebtRepository.save(existing);
        return deliveryDebtMapper.toDeliveryDebtResponseDto(updated);
    }

    @Override
    public DeliveryDebtResponseDto getDeliveryDebtById(Long id, Long deliveryId) {
        DeliveryDebt debt = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));

        DeliveryOrder order = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        if (!order.getDeliveryDebts().contains(debt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "delivery order", "id", deliveryId);
        }

        return deliveryDebtMapper.toDeliveryDebtResponseDto(debt);
    }

    @Override
    public void deleteDeliveryDebtById(Long id, Long deliveryId) {
        DeliveryDebt debt = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));

        DeliveryOrder order = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        if (!order.getDeliveryDebts().contains(debt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "delivery order", "id", deliveryId);
        }

        deliveryDebtRepository.delete(debt);
    }

    @Override
    public PagingResponse<DeliveryDebtResponseDto> getAllDeliveryDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long deliveryId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<DeliveryDebt> pages = deliveryDebtRepository.findAllByDeliveryOrderId(deliveryId, pageable);
        List<DeliveryDebtResponseDto> content = pages.getContent().stream()
                .map(deliveryDebtMapper::toDeliveryDebtResponseDto)
                .collect(Collectors.toList());

        PagingResponse<DeliveryDebtResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }
}
