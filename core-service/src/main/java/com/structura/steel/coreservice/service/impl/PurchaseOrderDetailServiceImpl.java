package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderDetailResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.entity.embedded.Product;
import com.structura.steel.coreservice.mapper.ProductMapper;
import com.structura.steel.coreservice.mapper.PurchaseOrderDetailMapper;
import com.structura.steel.coreservice.repository.PurchaseOrderDetailRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.service.PurchaseOrderDetailService;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseOrderDetailServiceImpl implements PurchaseOrderDetailService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    private final PurchaseOrderDetailMapper purchaseOrderDetailMapper;
    private final ProductMapper productMapper;

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

        return entityToResponseWithProduct(saved, dto.productId());
    }

    @Override
    public PurchaseOrderDetailResponseDto updatePurchaseOrderDetail(Long id, UpdatePurchaseOrderDetailRequestDto dto, Long purchaseId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order", "id", purchaseId));

        PurchaseOrderDetail existing = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderDetail", "id", id));

        purchaseOrderDetailMapper.updatePurchaseOrderDetailFromDto(dto, existing);
        existing.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
        PurchaseOrderDetail updated = purchaseOrderDetailRepository.save(existing);

        purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(updated.getSubtotal()));
        purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderDetailResponseDto result = purchaseOrderDetailMapper.toPurchaseOrderDetailResponseDto(updated);

        ProductResponseDto product = productMapper.toProductResponseDto(updated.getProduct());

        result.setProduct(product);

        return result;
    }

    @Override
    public PurchaseOrderDetailResponseDto getPurchaseOrderDetailById(Long id, Long purchaseId) {
        PurchaseOrderDetail detail = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderDetail", "id", id));

        PurchaseOrderDetailResponseDto result = purchaseOrderDetailMapper.toPurchaseOrderDetailResponseDto(detail);

        ProductResponseDto product = productMapper.toProductResponseDto(detail.getProduct());

        result.setProduct(product);

        return result;
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
                    .map(purchaseOrderDetailMapper::toPurchaseOrderDetailResponseDto)
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
                    .map(purchaseOrderDetailMapper::toPurchaseOrderDetailResponseDto)
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

        // 1. Lay tat ca product theo detail
        List<Long> productIds = batchDto.stream()
                .map(PurchaseOrderDetailRequestDto::productId)
                .distinct()
                .toList();

        // lấy product và chuyển sang map để get ở vòng lặp dưới
        Map<Long, ProductResponseDto> productResponseMap = Collections.emptyMap();

        if (!productIds.isEmpty()) {
            List<ProductResponseDto> productResponses = productFeignClient.getProductsBatch(productIds); // Gọi batch
            productResponseMap = productResponses.stream()
                    .collect(Collectors.toMap(ProductResponseDto::id, Function.identity())); // Tạo Map
        }

        // tinh toan khluong
        Map<Long, BigDecimal> weightMap = productIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        productFeignClient::getProductWeight
                ));
        // Convert sang entities
        List<PurchaseOrderDetail> entities = new ArrayList<>(batchDto.size());

        for (PurchaseOrderDetailRequestDto dto : batchDto) {

            ProductResponseDto productInfo = productResponseMap.get(dto.productId()); // Lấy từ Map
            if (productInfo == null) {
                throw new ResourceNotFoundException("Product", "id", dto.productId());
            }

            // Create the embedded Product snapshot
            Product productSnapshot = productMapper.toProductSnapShot(productInfo); // Convert sang Product embedded

            PurchaseOrderDetail e = purchaseOrderDetailMapper.toPurchaseOrderDetail(dto);
            e.setProduct(productSnapshot);

            BigDecimal unitWeight = weightMap.get(dto.productId());
            e.setWeight(unitWeight.multiply(dto.quantity()));
            e.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
            e.setPurchaseOrder(purchaseOrder);

            entities.add(e);
        }

        // 3. Luu tat ca
        List<PurchaseOrderDetail> saved = purchaseOrderDetailRepository.saveAll(entities);

        // 4. update tat ca
        BigDecimal newTotal = saved.stream()
                .map(PurchaseOrderDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount().add(newTotal));

        // 5.  Map to response
        return saved.stream()
                .map(d -> {
                    PurchaseOrderDetailResponseDto dto = purchaseOrderDetailMapper.toPurchaseOrderDetailResponseDto(d);
                    dto.setProduct(productMapper.toProductResponseDto(d.getProduct()));
                    return dto;
                })
                .toList();
    }

    private PurchaseOrderDetailResponseDto entityToResponseWithProduct(PurchaseOrderDetail detail, Long productId) {
        PurchaseOrderDetailResponseDto responseDto = purchaseOrderDetailMapper.toPurchaseOrderDetailResponseDto(detail);

        ProductResponseDto product = productFeignClient.getProductById(productId);
        responseDto.setProduct(product);

        return responseDto;
    }
}
