package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleDebt;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.mapper.SaleDebtMapper;
import com.structura.steel.coreservice.repository.SaleDebtRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleDebtService;
import com.structura.steel.commons.dto.core.request.SaleDebtRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllSaleDebtResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.dto.core.response.SaleDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleDebtServiceImpl implements SaleDebtService {

    private final SaleDebtRepository saleDebtRepository;
    private final SaleOrderRepository saleOrderRepository;

    private final SaleDebtMapper saleDebtMapper;

    private final ProductFeignClient productFeignClient;

    @Override
    public SaleDebtResponseDto createSaleDebt(SaleDebtRequestDto dto, Long saleId) {
        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        SaleDebt saleDebt = saleDebtMapper.toSaleDebt(dto);

        saleDebt.setSaleOrder(saleOrder);

        SaleDebt savedSaleDebt = saleDebtRepository.save(saleDebt);
        return entityToResponseWithProduct(savedSaleDebt);
    }

    @Override
    public SaleDebtResponseDto updateSaleDebt(Long id, SaleDebtRequestDto dto, Long saleId) {
        SaleDebt existing = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleDebts().contains(existing)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }
        saleDebtMapper.updateSaleDebtFromDto(dto, existing);
        SaleDebt updated = saleDebtRepository.save(existing);
        return entityToResponseWithProduct(updated);
    }

    @Override
    public SaleDebtResponseDto getSaleDebtById(Long id, Long saleId) {
        SaleDebt saleDebt = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleDebts().contains(saleDebt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        return entityToResponseWithProduct(saleDebt);
    }

    @Override
    public void deleteSaleDebtById(Long id, Long saleId) {
        SaleDebt saleDebt = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));
        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleDebts().contains(saleDebt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        saleDebtRepository.delete(saleDebt);
    }

    @Override
    public PagingResponse<GetAllSaleDebtResponseDto> getAllSaleDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SaleDebt> pages = saleDebtRepository.findAllBySaleOrderId(saleId, pageable);
        List<SaleDebt> saleDebts = pages.getContent();
        List<GetAllSaleDebtResponseDto> content = saleDebts.stream()
                .map(saleDebtMapper::toGetAllSaleDebtResponseDto)
                .collect(Collectors.toList());

        PagingResponse<GetAllSaleDebtResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }

    @Override
    public List<SaleDebtResponseDto> createSaleDebtsBatch(
            List<SaleDebtRequestDto> batchDto,
            Long saleId) {

        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        // 1. Map each DTO → entity, set relationship & any computed fields
        List<SaleDebt> entities = batchDto.stream()
                .map(dto -> {
                    SaleDebt debt = saleDebtMapper.toSaleDebt(dto);
                    debt.setSaleOrder(saleOrder);
                    return debt;
                })
                .toList();

        // 2. Save all in one go
        List<SaleDebt> saved = saleDebtRepository.saveAll(entities);

        // 3. (Optional) If you need to update saleOrder totals or other side-effects,
        //    do it here and save saleOrder.

        // 4. Map back to response DTOs (and enrich with product if needed)
        return saved.stream()
                .map(d -> {
                    SaleDebtResponseDto out = saleDebtMapper.toSaleDebtResponseDto(d);
                    // if you want to include product info:
                    // out.setProduct(productFeignClient.getProductById(d.getProductId()));
                    return out;
                })
                .toList();
    }

    private SaleDebtResponseDto entityToResponseWithProduct(SaleDebt debt) {
        SaleDebtResponseDto responseDto = saleDebtMapper.toSaleDebtResponseDto(debt);

        Long productId = debt.getProductId();
        ProductResponseDto product = productFeignClient.getProductById(productId);
        responseDto.setProduct(product);

        return responseDto;
    }
}
