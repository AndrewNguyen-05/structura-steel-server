package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.dto.core.response.PurchaseOrderDetailResponseDto;
import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.enumeration.DebtAccountType;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.PurchaseDebt;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import com.structura.steel.coreservice.mapper.PurchaseDebtMapper;
import com.structura.steel.coreservice.repository.PurchaseDebtRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.service.PurchaseDebtService;
import com.structura.steel.commons.dto.core.request.PurchaseDebtRequestDto;
import com.structura.steel.commons.dto.core.response.PurchaseDebtResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PurchaseDebtServiceImpl implements PurchaseDebtService {

    private final PurchaseDebtRepository purchaseDebtRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseDebtMapper purchaseDebtMapper;

    private final ProductFeignClient productFeignClient;
    private final PartnerFeignClient partnerFeignClient;

    @Override
    public PurchaseDebtResponseDto createPurchaseDebt(PurchaseDebtRequestDto dto, Long purchaseId) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        PurchaseDebt debt = purchaseDebtMapper.toPurchaseDebt(dto);
        debt.setPurchaseOrder(order);
        debt.setRemainingAmount(dto.originalAmount()); // set khoang no con lai
        debt.setStatus(DebtStatus.UNPAID); // set chua thanh toan

        PurchaseDebt saved = purchaseDebtRepository.save(debt);

        // **Update Partner Debt (Increase Payable)**
        Long partnerId = order.getSupplierId();
        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(dto.originalAmount(), DebtAccountType.PAYABLE));
            log.info("Increased payable debt for partner {} by {}", partnerId, dto.originalAmount());
        } catch (Exception e) {
            log.error("Failed to update partner {} debt for new purchase debt {}: {}",
                    partnerId, saved.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update partner debt. Purchase debt creation failed.", e);
        }

        return entityToResponseWithProduct(saved);
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

        // **Handle debt update logic carefully**
        // If original amount changes, we need to adjust partner debt
        BigDecimal oldOriginalAmount = existing.getOriginalAmount();
        BigDecimal newOriginalAmount = dto.originalAmount();
        BigDecimal difference = newOriginalAmount.subtract(oldOriginalAmount);

        // Cannot update if paid or cancelled
        if (existing.getStatus() == DebtStatus.PAID || existing.getStatus() == DebtStatus.CANCELLED) {
            throw new BadRequestException("Cannot update a debt that is already PAID or CANCELLED.");
        }

        purchaseDebtMapper.updatePurchaseDebtFromDto(dto, existing);
        // Recalculate remaining amount based on the difference
        existing.setRemainingAmount(existing.getRemainingAmount().add(difference));
        // Re-validate status (though unlikely to change here unless it becomes negative - which shouldn't happen)
        if (existing.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Updating debt resulted in a negative remaining amount.");
        }


        PurchaseDebt updated = purchaseDebtRepository.save(existing);

        // **Update Partner Debt if difference is not zero**
        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            Long partnerId = po.getSupplierId();
            try {
                partnerFeignClient.updatePartnerDebt(partnerId,
                        new UpdatePartnerDebtRequestDto(difference, DebtAccountType.PAYABLE));
                log.info("Updated payable debt for partner {} by {}", partnerId, difference);
            } catch (Exception e) {
                log.error("Failed to update partner {} debt for purchase debt update {}: {}",
                        partnerId, updated.getId(), e.getMessage(), e);
                throw new RuntimeException("Failed to update partner debt. Purchase debt update failed.", e);
            }
        }

        return entityToResponseWithProduct(updated);
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

        // **Cannot delete if partially or fully paid (consider cancellation instead?)**
        if (debt.getStatus() == DebtStatus.PARTIALLY_PAID || debt.getStatus() == DebtStatus.PAID) {
            throw new BadRequestException("Cannot delete a debt that has payments. Consider cancelling.");
        }

        BigDecimal amountToReverse = debt.getOriginalAmount();
        Long partnerId = debt.getPurchaseOrder().getSupplierId();

        purchaseDebtRepository.delete(debt);

        // **Reverse Partner Debt (Decrease Payable)**
        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(amountToReverse.negate(), DebtAccountType.PAYABLE));
            log.info("Reversed payable debt for partner {} by {}", partnerId, amountToReverse);
        } catch (Exception e) {
            log.error("Failed to reverse partner {} debt for deleted purchase debt {}: {}",
                    partnerId, id, e.getMessage(), e);
            // Decide how to handle - maybe log and continue or throw
            throw new RuntimeException("Failed to reverse partner debt. Deletion partially failed.", e);
        }
    }

    @Override
    public List<PurchaseDebtResponseDto> createPurchaseDebtsBatch(
            List<PurchaseDebtRequestDto> batchDto,
            Long purchaseId) {

        PurchaseOrder order = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseId));

        final BigDecimal[] totalBatchAmount = {BigDecimal.ZERO};

        List<PurchaseDebt> entities = batchDto.stream()
                .map(dto -> {
                    PurchaseDebt debt = purchaseDebtMapper.toPurchaseDebt(dto);
                    debt.setPurchaseOrder(order);
                    debt.setRemainingAmount(dto.originalAmount()); // **Set remaining**
                    debt.setStatus(DebtStatus.UNPAID); // **Set status**
                    totalBatchAmount[0] = totalBatchAmount[0].add(dto.originalAmount()); // **Sum total**
                    return debt;
                })
                .toList();

        List<PurchaseDebt> saved = purchaseDebtRepository.saveAll(entities);

        // **Update Partner Debt Once for the whole batch**
        Long partnerId = order.getSupplierId();
        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(totalBatchAmount[0], DebtAccountType.PAYABLE));
            log.info("Increased payable debt for partner {} by {} (Batch)", partnerId, totalBatchAmount[0]);
        } catch (Exception e) {
            log.error("Failed to update partner {} debt for batch purchase debt creation: {}",
                    partnerId, e.getMessage(), e);
            throw new RuntimeException("Failed to update partner debt. Batch creation failed.", e);
        }

        return saved.stream()
                .map(this::entityToResponseWithProduct)
                .toList();
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

        return entityToResponseWithProduct(debt);
    }

    @Override
    public PagingResponse<PurchaseDebtResponseDto> getAllPurchaseDebts(
            int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long purchaseId) {
        if(all) {
            List<PurchaseDebt> allDetails = purchaseDebtRepository.findAllByPurchaseOrderId(purchaseId);
            List<PurchaseDebtResponseDto> content = allDetails.stream()
                    .map(this::entityToResponseWithProduct)
                    .collect(Collectors.toList());

            // Tạo PagingResponse "giả" chứa tất cả
            PagingResponse<PurchaseDebtResponseDto> response = new PagingResponse<>();
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

            Page<PurchaseDebt> pages = purchaseDebtRepository.findAllByPurchaseOrderId(purchaseId, pageable);
            List<PurchaseDebtResponseDto> content = pages.getContent().stream()
                    .map(this::entityToResponseWithProduct)
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
    }

    private PurchaseDebtResponseDto entityToResponseWithProduct(PurchaseDebt debt) {
        PurchaseDebtResponseDto responseDto = purchaseDebtMapper.toPurchaseDebtResponseDto(debt);
        responseDto.setStatus(debt.getStatus().text());

        Long productId = debt.getProductId();
        ProductResponseDto product = productFeignClient.getProductById(productId);
        responseDto.setProduct(product);

        return responseDto;
    }
}
