package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleDebt;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.mapper.SaleDebtMapper;
import com.structura.steel.coreservice.repository.SaleDebtRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleDebtService;
import com.structura.steel.dto.request.SaleDebtRequestDto;
import com.structura.steel.dto.response.SaleDebtResponseDto;
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

    @Override
    public SaleDebtResponseDto createSaleDebt(SaleDebtRequestDto dto, Long saleId) {
        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        SaleDebt saleDebt = saleDebtMapper.toSaleDebt(dto);

        saleDebt.setSaleOrder(saleOrder);

        SaleDebt savedSaleDebt = saleDebtRepository.save(saleDebt);
        return saleDebtMapper.toSaleDebtResponseDto(savedSaleDebt);
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
        return saleDebtMapper.toSaleDebtResponseDto(updated);
    }

    @Override
    public SaleDebtResponseDto getSaleDebtById(Long id, Long saleId) {
        SaleDebt saleDebt = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));

        SaleOrder saleOrder = saleOrderRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleDebts().contains(saleDebt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        return saleDebtMapper.toSaleDebtResponseDto(saleDebt);
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
    public PagingResponse<SaleDebtResponseDto> getAllSaleDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SaleDebt> pages = saleDebtRepository.findAll(pageable);
        List<SaleDebt> saleDebts = pages.getContent();
        List<SaleDebtResponseDto> content = saleDebts.stream()
                .map(saleDebtMapper::toSaleDebtResponseDto)
                .collect(Collectors.toList());

        PagingResponse<SaleDebtResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }
}
