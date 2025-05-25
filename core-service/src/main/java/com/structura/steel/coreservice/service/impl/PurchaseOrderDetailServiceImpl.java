package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.dto.core.response.SaleOrderDetailResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.mapper.PurchaseOrderDetailMapper;
import com.structura.steel.coreservice.repository.PurchaseOrderDetailRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.service.PurchaseOrderDetailService;
import com.structura.steel.commons.dto.core.request.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.PurchaseOrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseOrderDetailServiceImpl implements PurchaseOrderDetailService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    private final PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    private final ProductFeignClient productFeignClient;

    @Override
    public PurchaseOrderDetailResponseDto createPurchaseOrderDetail(PurchaseOrderDetailRequestDto dto, Long purchaseId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order", "id", purchaseId));

        PurchaseOrderDetail detail = purchaseOrderDetailMapper.toPurchaseOrderDetail(dto);
        detail.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
        detail.setPurchaseOrder(purchaseOrder);

        PurchaseOrderDetail saved = purchaseOrderDetailRepository.save(detail);
        purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(detail.getSubtotal()));
        purchaseOrderRepository.save(purchaseOrder);

        return entityToResponseWithProduct(saved);
    }

    @Override
    public PurchaseOrderDetailResponseDto updatePurchaseOrderDetail(Long id, PurchaseOrderDetailRequestDto dto, Long purchaseId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order", "id", purchaseId));

        PurchaseOrderDetail existing = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderDetail", "id", id));

        purchaseOrderDetailMapper.updatePurchaseOrderDetailFromDto(dto, existing);
        existing.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
        PurchaseOrderDetail updated = purchaseOrderDetailRepository.save(existing);

        purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(updated.getSubtotal()));
        purchaseOrderRepository.save(purchaseOrder);

        return entityToResponseWithProduct(updated);
    }

    @Override
    public PurchaseOrderDetailResponseDto getPurchaseOrderDetailById(Long id, Long purchaseId) {
        PurchaseOrderDetail detail = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderDetail", "id", id));
        return entityToResponseWithProduct(detail);
    }

    @Override
    public void deletePurchaseOrderDetailById(Long id, Long purchaseId) {
        PurchaseOrderDetail detail = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderDetail", "id", id));
        purchaseOrderDetailRepository.delete(detail);
    }

    @Override
    public PagingResponse<PurchaseOrderDetailResponseDto> getAllPurchaseOrderDetails(
            int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long purchaseId) {
        if(all) {
            List<PurchaseOrderDetail> allDetails = purchaseOrderDetailRepository.findAllByPurchaseOrderId(purchaseId);
            List<PurchaseOrderDetailResponseDto> content = allDetails.stream()
                    .map(this::entityToResponseWithProduct)
                    .collect(Collectors.toList());

            // Tạo PagingResponse "giả" chứa tất cả
            PagingResponse<PurchaseOrderDetailResponseDto> response = new PagingResponse<>();
            response.setContent(content);
            response.setTotalElements((long) content.size());
            response.setPageNo(0);
            response.setPageSize(content.size()); // Page size = total
            response.setTotalPages(1);
            response.setLast(true);
            return response;
        } else {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

            Page<PurchaseOrderDetail> pages = purchaseOrderDetailRepository.findAllByPurchaseOrderId(purchaseId, pageable);
            List<PurchaseOrderDetailResponseDto> content = pages.getContent().stream()
                    .map(this::entityToResponseWithProduct)
                    .collect(Collectors.toList());

            PagingResponse<PurchaseOrderDetailResponseDto> response = new PagingResponse<>();
            response.setContent(content);
            response.setTotalElements(pages.getTotalElements());
            response.setPageNo(pages.getNumber());
            response.setPageSize(pages.getSize());
            response.setTotalPages(pages.getTotalPages());
            response.setLast(pages.isLast());
            return response;
        }
    }

    @Override
    public List<PurchaseOrderDetailResponseDto> createPurchaseOrderDetailsBatch(
            List<PurchaseOrderDetailRequestDto> batchDto,
            Long purchaseId) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order", "id", purchaseId));

        // 1. build all entities
        List<PurchaseOrderDetail> entities = batchDto.stream()
                .map(dto -> {
                    PurchaseOrderDetail detail = purchaseOrderDetailMapper.toPurchaseOrderDetail(dto);
                    detail.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
                    detail.setPurchaseOrder(purchaseOrder);
                    return detail;
                })
                .toList();

        // 2. save in one go
        List<PurchaseOrderDetail> saved = purchaseOrderDetailRepository.saveAll(entities);

        // 3. update purchase total once
        BigDecimal batchTotal = saved.stream()
                .map(PurchaseOrderDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(batchTotal));
        purchaseOrderRepository.save(purchaseOrder);

        // 4. map to response DTOs
        return saved.stream()
                .map(this::entityToResponseWithProduct)
                .toList();
    }

    private PurchaseOrderDetailResponseDto entityToResponseWithProduct(PurchaseOrderDetail detail) {
        PurchaseOrderDetailResponseDto responseDto = purchaseOrderDetailMapper.toPurchaseOrderDetailResponseDto(detail);

        Long productId = detail.getProductId();
        ProductResponseDto product = productFeignClient.getProductById(productId);
        responseDto.setProduct(product);

        return responseDto;
    }
}
