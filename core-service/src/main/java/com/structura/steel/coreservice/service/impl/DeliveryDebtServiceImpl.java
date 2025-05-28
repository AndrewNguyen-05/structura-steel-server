package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.dto.core.request.DeliveryDebtRequestDto;
import com.structura.steel.commons.dto.core.response.DeliveryDebtResponseDto; // Class DTO
import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.enumeration.DebtAccountType;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.DeliveryDebt;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.coreservice.mapper.DeliveryDebtMapper;
import com.structura.steel.coreservice.repository.DeliveryDebtRepository;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
import com.structura.steel.coreservice.service.DeliveryDebtService;
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
public class DeliveryDebtServiceImpl implements DeliveryDebtService {

    private final DeliveryDebtRepository deliveryDebtRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryDebtMapper deliveryDebtMapper;
    private final PartnerFeignClient partnerFeignClient;

    @Override
    public DeliveryDebtResponseDto createDeliveryDebt(DeliveryDebtRequestDto dto, Long deliveryId) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        DeliveryDebt debt = deliveryDebtMapper.toDeliveryDebt(dto);
        debt.setDeliveryOrder(deliveryOrder);
        debt.setRemainingAmount(dto.originalAmount());
        debt.setStatus(DebtStatus.UNPAID);

        DeliveryDebt savedDebt = deliveryDebtRepository.save(debt);

        Long partnerId = deliveryOrder.getPartnerId();
        if (partnerId == null) {
            log.error("Cannot update partner debt for DeliveryDebt ID {} as DeliveryOrder ID {} is missing partnerId (transporterId).",
                    savedDebt.getId(), deliveryId);
            throw new BadRequestException("Transporter (PartnerId) not found on DeliveryOrder ID: " + deliveryId);
        }

        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(dto.originalAmount(), DebtAccountType.PAYABLE));
            log.info("Increased payable debt for partner (transporter) {} by {} for DeliveryDebt ID {}",
                    partnerId, dto.originalAmount(), savedDebt.getId());
        } catch (Exception e) {
            log.error("Failed to update partner {} debt for new delivery debt {}: {}",
                    partnerId, savedDebt.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update partner debt. Delivery debt creation failed.", e);
        }

        return mapEntityToDto(savedDebt);
    }

    @Override
    public DeliveryDebtResponseDto updateDeliveryDebt(Long id, DeliveryDebtRequestDto dto, Long deliveryId) {
        DeliveryDebt existing = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        if (!existing.getDeliveryOrder().getId().equals(order.getId())) {
            throw new ResourceNotBelongToException("DeliveryDebt", "id", id, "DeliveryOrder", "id", deliveryId);
        }

        BigDecimal oldOriginalAmount = existing.getOriginalAmount();
        BigDecimal newOriginalAmount = dto.originalAmount();
        BigDecimal difference = newOriginalAmount.subtract(oldOriginalAmount);

        if (existing.getStatus() == DebtStatus.PAID || existing.getStatus() == DebtStatus.CANCELLED) {
            throw new BadRequestException("Cannot update a debt that is already PAID or CANCELLED.");
        }

        deliveryDebtMapper.updateDeliveryDebtFromDto(dto, existing);
        existing.setRemainingAmount(existing.getRemainingAmount().add(difference));

        if (existing.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Updating debt resulted in a negative remaining amount.");
        }
        if (existing.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 && newOriginalAmount.compareTo(BigDecimal.ZERO) > 0) {
            existing.setStatus(DebtStatus.PAID);
        }


        DeliveryDebt updated = deliveryDebtRepository.save(existing);

        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            Long partnerId = order.getPartnerId();
            if (partnerId == null) {
                log.error("Cannot update partner debt for DeliveryDebt ID {} as DeliveryOrder ID {} is missing partnerId (transporterId).",
                        updated.getId(), deliveryId);
                throw new BadRequestException("Transporter (PartnerId) not found on DeliveryOrder ID: " + deliveryId);
            }
            try {
                partnerFeignClient.updatePartnerDebt(partnerId,
                        new UpdatePartnerDebtRequestDto(difference, DebtAccountType.PAYABLE));
                log.info("Updated payable debt for partner (transporter) {} by {}", partnerId, difference);
            } catch (Exception e) {
                log.error("Failed to update partner {} debt for delivery debt update {}: {}",
                        partnerId, updated.getId(), e.getMessage(), e);
                throw new RuntimeException("Failed to update partner debt. Delivery debt update failed.", e);
            }
        }
        return mapEntityToDto(updated);
    }

    @Override
    public DeliveryDebtResponseDto getDeliveryDebtById(Long id, Long deliveryId) {
        DeliveryDebt debt = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));
        if (!debt.getDeliveryOrder().getId().equals(deliveryId)) {
            throw new ResourceNotBelongToException("DeliveryDebt", "id", id, "DeliveryOrder", "id", deliveryId);
        }
        return mapEntityToDto(debt);
    }

    @Override
    public void deleteDeliveryDebtById(Long id, Long deliveryId) {
        DeliveryDebt debt = deliveryDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryDebt", "id", id));
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        if (!debt.getDeliveryOrder().getId().equals(order.getId())) {
            throw new ResourceNotBelongToException("DeliveryDebt", "id", id, "DeliveryOrder", "id", deliveryId);
        }

        if (debt.getStatus() == DebtStatus.PARTIALLY_PAID || debt.getStatus() == DebtStatus.PAID) {
            throw new BadRequestException("Cannot delete a debt that has payments. Consider cancelling.");
        }

        BigDecimal amountToReverse = debt.getOriginalAmount();
        Long partnerId = order.getPartnerId();
        if (partnerId == null) {
            log.error("Cannot reverse partner debt for DeliveryDebt ID {} as DeliveryOrder ID {} is missing partnerId (transporterId).",
                    id, deliveryId);
            throw new BadRequestException("Transporter (PartnerId) not found on DeliveryOrder ID: " + deliveryId + ". Cannot reverse partner debt.");
        }

        deliveryDebtRepository.delete(debt);

        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(amountToReverse.negate(), DebtAccountType.PAYABLE));
            log.info("Reversed payable debt for partner (transporter) {} by {}", partnerId, amountToReverse);
        } catch (Exception e) {
            log.error("Failed to reverse partner {} debt for deleted delivery debt {}: {}",
                    partnerId, id, e.getMessage(), e);
            throw new RuntimeException("Failed to reverse partner debt. Deletion partially failed.", e);
        }
    }

    @Override
    public PagingResponse<DeliveryDebtResponseDto> getAllDeliveryDebts(
            int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long deliveryId) {

        deliveryOrderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", deliveryId));

        Page<DeliveryDebt> pages;
        if (all) {
            List<DeliveryDebt> allDetails = deliveryDebtRepository.findAllByDeliveryOrderId(deliveryId);
            // Sửa pageSize để tránh lỗi nếu allDetails rỗng
            pages = new PageImpl<>(allDetails, PageRequest.of(0, Math.max(1, allDetails.size())), allDetails.size());
        } else {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            pages = deliveryDebtRepository.findAllByDeliveryOrderId(deliveryId, pageable);
        }

        List<DeliveryDebtResponseDto> content = pages.getContent().stream()
                .map(this::mapEntityToDto)
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

    // Helper method to map Entity to DTO and set String status
    private DeliveryDebtResponseDto mapEntityToDto(DeliveryDebt debt) {
        // Sử dụng mapper để chuyển đổi các trường cơ bản
        DeliveryDebtResponseDto dto = deliveryDebtMapper.toDeliveryDebtResponseDto(debt);

        // Sau đó, set trường status (String) từ enum
        if (debt.getStatus() != null) {
            dto.setStatus(debt.getStatus().text());
        } else {
            dto.setStatus(null); // Hoặc một giá trị mặc định nếu cần
        }

        return dto;
    }
}