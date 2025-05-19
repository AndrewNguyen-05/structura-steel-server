package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.PurchaseDebt;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.mapper.PurchaseDebtMapper;
import com.structura.steel.coreservice.repository.PurchaseDebtRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.service.PurchaseDebtService;
import com.structura.steel.dto.request.PurchaseDebtRequestDto;
import com.structura.steel.dto.response.PurchaseDebtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseDebtServiceImpl implements PurchaseDebtService {

    private final PurchaseDebtRepository purchaseDebtRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseDebtMapper purchaseDebtMapper;

    @Override
    public PurchaseDebtResponseDto createPurchaseDebt(PurchaseDebtRequestDto dto, Long purchaseId) {
        purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        PurchaseDebt debt = purchaseDebtMapper.toPurchaseDebt(dto);
        PurchaseDebt saved = purchaseDebtRepository.save(debt);
        return purchaseDebtMapper.toPurchaseDebtResponseDto(saved);
    }

    @Override
    public PurchaseDebtResponseDto updatePurchaseDebt(Long id, PurchaseDebtRequestDto dto, Long purchaseId) {
        PurchaseDebt existing = purchaseDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseDebt", "id", id));

        PurchaseOrder po = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        if (!po.getPurchaseDebts().contains(existing)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "purchase order", "id", purchaseId);
        }

        purchaseDebtMapper.updatePurchaseDebtFromDto(dto, existing);
        PurchaseDebt updated = purchaseDebtRepository.save(existing);
        return purchaseDebtMapper.toPurchaseDebtResponseDto(updated);
    }

    @Override
    public PurchaseDebtResponseDto getPurchaseDebtById(Long id, Long purchaseId) {
        PurchaseDebt debt = purchaseDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseDebt", "id", id));

        PurchaseOrder po = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        if (!po.getPurchaseDebts().contains(debt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "purchase order", "id", purchaseId);
        }

        return purchaseDebtMapper.toPurchaseDebtResponseDto(debt);
    }

    @Override
    public void deletePurchaseDebtById(Long id, Long purchaseId) {
        PurchaseDebt debt = purchaseDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseDebt", "id", id));

        PurchaseOrder po = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        if (!po.getPurchaseDebts().contains(debt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "purchase order", "id", purchaseId);
        }

        purchaseDebtRepository.delete(debt);
    }

    @Override
    public PagingResponse<PurchaseDebtResponseDto> getAllPurchaseDebts(int pageNo, int pageSize, String sortBy, String sortDir, Long purchaseId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PurchaseDebt> pages = purchaseDebtRepository.findAllByPurchaseOrderId(purchaseId, pageable);
        List<PurchaseDebtResponseDto> content = pages.getContent().stream()
                .map(purchaseDebtMapper::toPurchaseDebtResponseDto)
                .collect(Collectors.toList());

        PagingResponse<PurchaseDebtResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }

    @Override
    public List<PurchaseDebtResponseDto> createPurchaseDebtsBatch(
            List<PurchaseDebtRequestDto> batchDto,
            Long purchaseId) {

        // 1. ensure parent exists
        PurchaseOrder po = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        // 2. map DTOs → entities
        List<PurchaseDebt> entities = batchDto.stream()
                .map(dto -> {
                    PurchaseDebt debt = purchaseDebtMapper.toPurchaseDebt(dto);
                    debt.setPurchaseOrder(po);
                    return debt;
                })
                .toList();

        // 3. save all at once
        List<PurchaseDebt> saved = purchaseDebtRepository.saveAll(entities);

        // 4. (optional) any side-effects on PurchaseOrder? e.g. update totals

        // 5. map back to response DTOs
        return saved.stream()
                .map(purchaseDebtMapper::toPurchaseDebtResponseDto)
                .toList();
    }

}
