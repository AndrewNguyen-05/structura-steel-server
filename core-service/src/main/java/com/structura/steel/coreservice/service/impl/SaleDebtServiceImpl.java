package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.client.ProductFeignClient;
import com.structura.steel.commons.dto.core.request.sale.SaleDebtRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseDebtResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleDebtResponseDto;
import com.structura.steel.commons.dto.partner.request.UpdatePartnerDebtRequestDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.commons.enumeration.DebtAccountType;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleDebt;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.embedded.Product;
import com.structura.steel.coreservice.mapper.ProductMapper;
import com.structura.steel.coreservice.mapper.SaleDebtMapper;
import com.structura.steel.coreservice.repository.SaleDebtRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleDebtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j // Add
public class SaleDebtServiceImpl implements SaleDebtService {

    private final SaleDebtRepository saleDebtRepository;
    private final SaleOrderRepository saleOrderRepository;

    private final ProductMapper productMapper;
    private final SaleDebtMapper saleDebtMapper;

    private final ProductFeignClient productFeignClient;
    private final PartnerFeignClient partnerFeignClient;

    @Override
    public SaleDebtResponseDto createSaleDebt(SaleDebtRequestDto dto, Long saleId) {
        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        SaleDebt saleDebt = saleDebtMapper.toSaleDebt(dto);
        saleDebt.setSaleOrder(saleOrder);
        saleDebt.setRemainingAmount(dto.originalAmount());
        saleDebt.setStatus(DebtStatus.UNPAID);

        SaleDebt savedSaleDebt = saleDebtRepository.save(saleDebt);

        // Update Partner Debt (Increase Receivable)
        Long partnerId = saleOrder.getPartner().id(); // Partner is the customer
        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(dto.originalAmount(), DebtAccountType.RECEIVABLE));
            log.info("Increased receivable debt for partner {} by {}", partnerId, dto.originalAmount());
        } catch (Exception e) {
            log.error("Failed to update partner {} debt for new sale debt {}: {}",
                    partnerId, savedSaleDebt.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update partner debt. Sale debt creation failed.", e);
        }

        return entityToResponseWithProduct(savedSaleDebt, dto.productId());
    }

    @Override
    public SaleDebtResponseDto updateSaleDebt(Long id, SaleDebtRequestDto dto, Long saleId) {
        SaleDebt existing = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));
        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        if (!saleOrder.getSaleDebts().contains(existing)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        BigDecimal oldOriginalAmount = existing.getOriginalAmount();
        BigDecimal newOriginalAmount = dto.originalAmount();
        BigDecimal difference = newOriginalAmount.subtract(oldOriginalAmount);

        if (existing.getStatus() == DebtStatus.PAID || existing.getStatus() == DebtStatus.CANCELLED) {
            throw new BadRequestException("Cannot update a debt that is already PAID or CANCELLED.");
        }

        saleDebtMapper.updateSaleDebtFromDto(dto, existing);
        existing.setRemainingAmount(existing.getRemainingAmount().add(difference));

        if (existing.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Updating debt resulted in a negative remaining amount.");
        }
        // Consider updating status if remainingAmount becomes 0 due to originalAmount decrease
        if (existing.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 && newOriginalAmount.compareTo(BigDecimal.ZERO) > 0) {
            existing.setStatus(DebtStatus.PAID);
        } else if (existing.getRemainingAmount().compareTo(existing.getOriginalAmount()) < 0 && existing.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
            existing.setStatus(DebtStatus.PARTIALLY_PAID); // This case might be tricky if originalAmount changed
        }


        SaleDebt updated = saleDebtRepository.save(existing);

        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            Long partnerId = saleOrder.getPartner().id();
            try {
                partnerFeignClient.updatePartnerDebt(partnerId,
                        new UpdatePartnerDebtRequestDto(difference, DebtAccountType.RECEIVABLE));
                log.info("Updated receivable debt for partner {} by {}", partnerId, difference);
            } catch (Exception e) {
                log.error("Failed to update partner {} debt for sale debt update {}: {}",
                        partnerId, updated.getId(), e.getMessage(), e);
                throw new RuntimeException("Failed to update partner debt. Sale debt update failed.", e);
            }
        }

        SaleDebtResponseDto result = saleDebtMapper.toSaleDebtResponseDto(updated);

        ProductResponseDto product = productMapper.toProductResponseDto(updated.getProduct());

        result.setProduct(product);

        return result;
    }

    @Override
    public void deleteSaleDebtById(Long id, Long saleId) {
        SaleDebt saleDebt = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));
        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        if (!saleOrder.getSaleDebts().contains(saleDebt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        if (saleDebt.getStatus() == DebtStatus.PARTIALLY_PAID || saleDebt.getStatus() == DebtStatus.PAID) {
            throw new BadRequestException("Cannot delete a debt that has payments. Consider cancelling or creating a credit note.");
        }

        BigDecimal amountToReverse = saleDebt.getOriginalAmount();
        Long partnerId = saleOrder.getPartner().id();

        saleDebtRepository.delete(saleDebt);

        try {
            partnerFeignClient.updatePartnerDebt(partnerId,
                    new UpdatePartnerDebtRequestDto(amountToReverse.negate(), DebtAccountType.RECEIVABLE));
            log.info("Reversed receivable debt for partner {} by {}", partnerId, amountToReverse);
        } catch (Exception e) {
            log.error("Failed to reverse partner {} debt for deleted sale debt {}: {}",
                    partnerId, id, e.getMessage(), e);
            throw new RuntimeException("Failed to reverse partner debt. Deletion partially failed.", e);
        }
    }

    @Override
    public List<SaleDebtResponseDto> createSaleDebtsBatch(
            List<SaleDebtRequestDto> batchDto,
            Long saleId) {

        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));

        // 1. Lấy tất cả ID sản phẩm từ batch request
        List<Long> productIds = batchDto.stream()
                .map(SaleDebtRequestDto::productId)
                .distinct()
                .toList();

        // 2. Gọi Product service MỘT LẦN để lấy tất cả thông tin sản phẩm
        Map<Long, ProductResponseDto> productMap = productFeignClient.getProductsBatch(productIds).stream()
                .collect(Collectors.toMap(ProductResponseDto::id, Function.identity()));

        // 3. Tạo danh sách các thực thể SaleDebt và nhúng thông tin Product
        List<SaleDebt> entities = batchDto.stream()
                .map(dto -> {
                    SaleDebt debt = saleDebtMapper.toSaleDebt(dto);
                    debt.setSaleOrder(saleOrder);
                    debt.setRemainingAmount(dto.originalAmount());
                    debt.setStatus(DebtStatus.UNPAID);

                    // Lấy thông tin product đầy đủ từ map
                    ProductResponseDto productDto = productMap.get(dto.productId());
                    if (productDto != null) {
                        // Giả định productMapper.toProductSnapShot đã được sửa để nhận ProductResponseDto
                        // và trả về entity Product để lưu JSONB
                        Product productSnapshot = productMapper.toProductSnapShot(productDto);
                        debt.setProduct(productSnapshot);
                    } else {
                        log.warn("Product with ID {} not found for sale debt.", dto.productId());
                        // Có thể throw exception ở đây nếu product là bắt buộc
                    }
                    return debt;
                })
                .toList();

        // 4. Lưu tất cả các entity vào DB trong một transaction
        List<SaleDebt> savedDebts = saleDebtRepository.saveAll(entities);

        // 5. Tính tổng số tiền của cả batch một cách an toàn
        BigDecimal totalBatchAmount = savedDebts.stream()
                .map(SaleDebt::getOriginalAmount)
                .filter(Objects::nonNull) // Lọc bỏ các giá trị null
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 6. Cập nhật công nợ cho Partner MỘT LẦN DUY NHẤT
        if (totalBatchAmount.compareTo(BigDecimal.ZERO) > 0) {
            Long partnerId = saleOrder.getPartner().id();
            try {
                partnerFeignClient.updatePartnerDebt(partnerId,
                        new UpdatePartnerDebtRequestDto(totalBatchAmount, DebtAccountType.RECEIVABLE));
                log.info("Increased receivable debt for partner {} by {} (Batch)", partnerId, totalBatchAmount);
            } catch (Exception e) {
                log.error("Failed to update partner {} debt for batch sale debt creation: {}",
                        partnerId, e.getMessage(), e);
                throw new RuntimeException("Failed to update partner debt. Batch creation failed.", e);
            }
        }

        // 7. Map kết quả trả về, thông tin product đã có sẵn, không cần gọi API nữa
        return savedDebts.stream()
                .map(savedDebt -> {
                    SaleDebtResponseDto responseDto = saleDebtMapper.toSaleDebtResponseDto(savedDebt);
                    // productMapper.toProductResponseDto sẽ chuyển từ entity Product (JSONB) sang DTO
                    responseDto.setProduct(productMapper.toProductResponseDto(savedDebt.getProduct()));
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SaleDebtResponseDto getSaleDebtById(Long id, Long saleId) {
        SaleDebt saleDebt = saleDebtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleDebt", "id", id));
        SaleOrder saleOrder = saleOrderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", saleId));
        if(!saleOrder.getSaleDebts().contains(saleDebt)) {
            throw new ResourceNotBelongToException("Debt", "id", id, "sale order", "id", saleId);
        }

        SaleDebtResponseDto result = saleDebtMapper.toSaleDebtResponseDto(saleDebt);

        ProductResponseDto product = productMapper.toProductResponseDto(saleDebt.getProduct());

        result.setProduct(product);

        return result;
    }

    @Override
    public PagingResponse<SaleDebtResponseDto> getAllSaleDebts(
            int pageNo, int pageSize, String sortBy, String sortDir, boolean all, Long saleId) {
        if(all) {
            List<SaleDebt> allDetails = saleDebtRepository.findAllBySaleOrderId(saleId);
            List<SaleDebtResponseDto> content = allDetails.stream()
                    .map(saleDebtMapper::toSaleDebtResponseDto)
                    .collect(Collectors.toList());

            PagingResponse<SaleDebtResponseDto> response = new PagingResponse<>();
            response.setContent(content);
            response.setTotalElements((long) content.size());
            response.setPageNo(0);
            response.setPageSize(content.size());
            response.setTotalPages(1);
            response.setLast(true);
            return response;
        } else {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<SaleDebt> pages = saleDebtRepository.findAllBySaleOrderId(saleId, pageable);
            List<SaleDebtResponseDto> content = pages.getContent().stream() // Changed variable name
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

    private SaleDebtResponseDto entityToResponseWithProduct(SaleDebt debt, Long productId) {
        SaleDebtResponseDto responseDto = saleDebtMapper.toSaleDebtResponseDto(debt);
        if (debt.getStatus() != null) { // Check for null status before calling text()
            responseDto.setStatus(debt.getStatus().text());
        } else {
            responseDto.setStatus(null); // Or some default string like "N/A"
        }

        if (productId != null && productFeignClient != null) {
            try {
                ProductResponseDto product = productFeignClient.getProductById(productId);
                responseDto.setProduct(product);
            } catch (Exception e) {
                log.error("Failed to fetch product info for product ID {}: {}", productId, e.getMessage());
                // Optionally set product to null or a default error representation
                responseDto.setProduct(null);
            }
        }
        return responseDto;
    }
}