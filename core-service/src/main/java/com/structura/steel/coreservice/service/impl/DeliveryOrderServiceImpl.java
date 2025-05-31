package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.DeliveryOrderSearchRepository;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.coreservice.mapper.DeliveryOrderMapper;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
import com.structura.steel.coreservice.service.DeliveryOrderService;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.delivery.GetAllDeliveryOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderMapper deliveryOrderMapper;
    private final DeliveryOrderSearchRepository deliveryOrderSearchRepository;

    @Override
    public DeliveryOrderResponseDto createDeliveryOrder(DeliveryOrderRequestDto dto) {
        DeliveryOrder order = deliveryOrderMapper.toDeliveryOrder(dto);
        order.setDeliveryCode(CodeGenerator.generateCode(EntityType.DELIVERY));
        DeliveryOrder saved = deliveryOrderRepository.save(order);
        return deliveryOrderMapper.toDeliveryOrderResponseDto(saved);
    }

    @Override
    public DeliveryOrderResponseDto updateDeliveryOrder(Long id, DeliveryOrderRequestDto dto) {
        DeliveryOrder existing = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));
        deliveryOrderMapper.updateDeliveryOrderFromDto(dto, existing);
        DeliveryOrder updated = deliveryOrderRepository.save(existing);
        return deliveryOrderMapper.toDeliveryOrderResponseDto(updated);
    }

    @Override
    public DeliveryOrderResponseDto getDeliveryOrderById(Long id) {
        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));
        return deliveryOrderMapper.toDeliveryOrderResponseDto(order);
    }

    @Override
    public void deleteDeliveryOrderById(Long id) {
        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));
        deliveryOrderRepository.delete(order);
    }

    @Override
    public PagingResponse<GetAllDeliveryOrderResponseDto> getAllDeliveryOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<DeliveryOrder> pages = deliveryOrderRepository.findAll(pageable);
        List<GetAllDeliveryOrderResponseDto> content = pages.getContent().stream()
                .map(deliveryOrderMapper::toGetAllDeliveryOrderResponseDto)
                .collect(Collectors.toList());

        PagingResponse<GetAllDeliveryOrderResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> suggestDeliveries(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = deliveryOrderSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(DeliveryOrderDocument::getDeliveryCode)
                .distinct()
                .toList();
    }
}
