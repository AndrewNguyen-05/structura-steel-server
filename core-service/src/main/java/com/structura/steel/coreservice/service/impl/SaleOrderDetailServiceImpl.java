package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.mapper.SaleOrderDetailMapper;
import com.structura.steel.coreservice.repository.SaleOrderDetailRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleOrderDetailService;
import com.structura.steel.commons.dto.core.request.SaleOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllSaleOrderDetailResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.dto.core.response.SaleOrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleOrderDetailServiceImpl implements SaleOrderDetailService {

    private final SaleOrderDetailRepository saleOrderDetailRepository;
    private final SaleOrderRepository saleOrderRepository;

    private final SaleOrderDetailMapper saleOrderDetailMapper;

    private final ProductFeignClient productFeignClient;

    @Override
    public SaleOrderDetailResponseDto createSaleOrderDetail(SaleOrderDetailRequestDto dto, Long saleId) {
        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        SaleOrderDetail saleOrderDetail = saleOrderDetailMapper.toSaleOrderDetail(dto);

        BigDecimal productWeight = productFeignClient.getProductWeight(dto.productId());
        if (productWeight == null) {
            throw new RuntimeException("Could not fetch weight for product id: " + dto.productId());
        }
        saleOrderDetail.setWeight(dto.quantity().multiply(productWeight));
        saleOrderDetail.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
        saleOrderDetail.setSaleOrder(saleOrder);

        SaleOrderDetail savedSaleOrderDetail = saleOrderDetailRepository.save(saleOrderDetail);
        saleOrder.setTotalAmount(saleOrder.getTotalAmount().add(saleOrderDetail.getSubtotal()));
        return entityToResponseWithProduct(savedSaleOrderDetail);
    }

    @Override
    public SaleOrderDetailResponseDto updateSaleOrderDetail(Long id, SaleOrderDetailRequestDto dto, Long saleId) {
        SaleOrderDetail existing = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleOrderDetails().contains(existing)) {
            throw new ResourceNotBelongToException("Sale detail", "id", id, "sale order", "id", saleId);
        }

        saleOrderDetailMapper.updateSaleOrderDetailFromDto(dto, existing);
        SaleOrderDetail updated = saleOrderDetailRepository.save(existing);
        return entityToResponseWithProduct(updated);
    }

    @Override
    public SaleOrderDetailResponseDto getSaleOrderDetailById(Long id, Long saleId) {
        SaleOrderDetail saleOrderDetail = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleOrderDetails().contains(saleOrderDetail)) {
            throw new ResourceNotBelongToException("Sale detail", "id", id, "sale order", "id", saleId);
        }
        return entityToResponseWithProduct(saleOrderDetail);
    }

    @Override
    public void deleteSaleOrderDetailById(Long id, Long saleId) {
        SaleOrderDetail saleOrderDetail = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleOrderDetails().contains(saleOrderDetail)) {
            throw new ResourceNotBelongToException("Sale detail", "id", id, "sale order", "id", saleId);
        }
        saleOrderDetailRepository.delete(saleOrderDetail);
    }

    @Override
    public PagingResponse<GetAllSaleOrderDetailResponseDto> getAllSaleOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SaleOrderDetail> pages = saleOrderDetailRepository.findAllBySaleOrderId(saleId, pageable);
        List<SaleOrderDetail> saleOrderDetails = pages.getContent();
        List<GetAllSaleOrderDetailResponseDto> content = saleOrderDetails.stream()
                .map(saleOrderDetailMapper::toSaleOrderDetailGetAllDto)
                .collect(Collectors.toList());
        PagingResponse<GetAllSaleOrderDetailResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }

    @Override
    public List<SaleOrderDetailResponseDto> createSaleOrderDetailsBatch(List<SaleOrderDetailRequestDto> batchDto, Long saleId) {

        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        // 1. Lay tat ca product theo detail
        List<Long> productIds = batchDto.stream()
                .map(SaleOrderDetailRequestDto::productId)
                .distinct()
                .toList();

        List<ProductResponseDto> products = productFeignClient.getProductsBatch(productIds);

        Map<Long, ProductResponseDto> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponseDto::id, Function.identity()));

        // tinh toan khluong
        Map<Long, BigDecimal> weightMap = productIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        productFeignClient::getProductWeight
                ));

        // 2. Convert sang entities
        List<SaleOrderDetail> entities = new ArrayList<>(batchDto.size());

        for (SaleOrderDetailRequestDto dto : batchDto) {
            SaleOrderDetail e = saleOrderDetailMapper.toSaleOrderDetail(dto);

            BigDecimal unitWeight = weightMap.get(dto.productId());
            e.setWeight(unitWeight.multiply(dto.quantity()));
            e.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
            e.setSaleOrder(saleOrder);

            entities.add(e);
        }

        // 3. Luu tat ca
        List<SaleOrderDetail> saved = saleOrderDetailRepository.saveAll(entities);

        // 4. update tat ca
        BigDecimal newTotal = saved.stream()
                .map(SaleOrderDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        saleOrder.setTotalAmount(saleOrder.getTotalAmount().add(newTotal));

        // 5.  Map to response
        return saved.stream()
                .map(d -> {
                    SaleOrderDetailResponseDto dto = saleOrderDetailMapper.toSaleOrderDetailResponseDto(d);
                    dto.setProduct(productMap.get(d.getProductId()));
                    return dto;
                })
                .toList();
    }

    private SaleOrderDetailResponseDto entityToResponseWithProduct(SaleOrderDetail detail) {
        SaleOrderDetailResponseDto responseDto = saleOrderDetailMapper.toSaleOrderDetailResponseDto(detail);

        Long productId = detail.getProductId();
        ProductResponseDto product = productFeignClient.getProductById(productId);
        responseDto.setProduct(product);

        return responseDto;
    }
}
