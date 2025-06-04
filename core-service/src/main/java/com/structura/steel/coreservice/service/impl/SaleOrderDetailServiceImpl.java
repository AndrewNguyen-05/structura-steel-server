package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.entity.embedded.Product;
import com.structura.steel.coreservice.mapper.ProductMapper;
import com.structura.steel.coreservice.mapper.SaleOrderDetailMapper;
import com.structura.steel.coreservice.repository.SaleOrderDetailRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleOrderDetailService;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderDetailRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderDetailResponseDto;
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
public class SaleOrderDetailServiceImpl implements SaleOrderDetailService {

    private final SaleOrderDetailRepository saleOrderDetailRepository;
    private final SaleOrderRepository saleOrderRepository;

    private final SaleOrderDetailMapper saleOrderDetailMapper;

    private final ProductFeignClient productFeignClient;
    private final ProductMapper productMapper;

    @Override
    public SaleOrderDetailResponseDto createSaleOrderDetail(SaleOrderDetailRequestDto dto, Long saleId) {
        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        SaleOrderDetail detail = saleOrderDetailMapper.toSaleOrderDetail(dto);

        ProductResponseDto productResponse = productFeignClient.getProductById(dto.productId());
        Product product = productMapper.toProductSnapShot(productResponse);

        BigDecimal productWeight = productFeignClient.getProductWeight(dto.productId());
        if (productWeight == null) {
            throw new RuntimeException("Could not fetch weight for product id: " + dto.productId());
        }
        detail.setWeight(dto.quantity().multiply(productWeight));
        detail.setSubtotal(dto.quantity().multiply(dto.unitPrice()));
        detail.setSaleOrder(saleOrder);
        detail.setProduct(product);

        SaleOrderDetail saved = saleOrderDetailRepository.save(detail);
        saleOrder.setTotalAmount(saleOrder.getTotalAmount().add(detail.getSubtotal()));

        SaleOrderDetailResponseDto result = saleOrderDetailMapper.toSaleOrderDetailResponseDto(saved);
        result.setProduct(productResponse);

        return result;
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

        ProductResponseDto responseDto = productMapper.toProductResponseDto(updated.getProduct());
        SaleOrderDetailResponseDto result = saleOrderDetailMapper.toSaleOrderDetailResponseDto(updated);
        result.setProduct(responseDto);

        return result;
    }

    @Override
    public SaleOrderDetailResponseDto getSaleOrderDetailById(Long id, Long saleId) {
        SaleOrderDetail detail = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleOrderDetails().contains(detail)) {
            throw new ResourceNotBelongToException("Sale detail", "id", id, "sale order", "id", saleId);
        }

        ProductResponseDto responseDto = productMapper.toProductResponseDto(detail.getProduct());
        SaleOrderDetailResponseDto result = saleOrderDetailMapper.toSaleOrderDetailResponseDto(detail);
        result.setProduct(responseDto);

        return result;
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
    public PagingResponse<SaleOrderDetailResponseDto> getAllSaleOrderDetails(
            int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long saleId) {
        if(all) {
            List<SaleOrderDetail> allDetails = saleOrderDetailRepository.findAllBySaleOrderId(saleId);
            List<SaleOrderDetailResponseDto> content = allDetails.stream()
                    .map(detail -> {
                        SaleOrderDetailResponseDto dto = saleOrderDetailMapper.toSaleOrderDetailResponseDto(detail);
                        ProductResponseDto responseDto = productMapper.toProductResponseDto(detail.getProduct());
                        dto.setProduct(responseDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            // Tạo PagingResponse "giả" chứa tất cả
            PagingResponse<SaleOrderDetailResponseDto> response = new PagingResponse<>();
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
            Page<SaleOrderDetail> pages = saleOrderDetailRepository.findAllBySaleOrderId(saleId, pageable);
            List<SaleOrderDetail> saleOrderDetails = pages.getContent();
            List<SaleOrderDetailResponseDto> content = saleOrderDetails.stream()
                    .map(detail -> {
                        SaleOrderDetailResponseDto dto = saleOrderDetailMapper.toSaleOrderDetailResponseDto(detail);
                        ProductResponseDto responseDto = productMapper.toProductResponseDto(detail.getProduct());
                        dto.setProduct(responseDto);
                        return dto;
                    })
                    .collect(Collectors.toList());
            PagingResponse<SaleOrderDetailResponseDto> response = new PagingResponse<>();
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
    public List<SaleOrderDetailResponseDto> createSaleOrderDetailsBatch(List<SaleOrderDetailRequestDto> batchDto, Long saleId) {

        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        // 1. Lay tat ca product theo detail
        List<Long> productIds = batchDto.stream()
                .map(SaleOrderDetailRequestDto::productId)
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

        // 2. Convert sang entities
        List<SaleOrderDetail> entities = new ArrayList<>(batchDto.size());

        for (SaleOrderDetailRequestDto dto : batchDto) {

            ProductResponseDto productInfo = productResponseMap.get(dto.productId()); // Lấy từ Map
            if (productInfo == null) {
                throw new ResourceNotFoundException("Product", "id", dto.productId());
            }

            // Create the embedded Product snapshot
            Product productSnapshot = productMapper.toProductSnapShot(productInfo); // Convert sang Product embedded

            SaleOrderDetail e = saleOrderDetailMapper.toSaleOrderDetail(dto);
            e.setProduct(productSnapshot);

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
                    dto.setProduct(productMapper.toProductResponseDto(d.getProduct()));
                    return dto;
                })
                .toList();
    }
}
