package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.dto.core.request.sale.UpdateSaleOrderRequestDto;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.SaleOrderSearchRepository;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.PartnerProject;
import com.structura.steel.coreservice.event.PurchaseOrderCreatedEvent;
import com.structura.steel.coreservice.mapper.PartnerMapper;
import com.structura.steel.coreservice.mapper.SaleOrderMapper;
import com.structura.steel.coreservice.repository.SaleDebtRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.SaleOrderService;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderRequestDto;
import com.structura.steel.commons.dto.core.response.sale.GetAllSaleOrderResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final SaleDebtRepository saleDebtRepository;

    private final SaleOrderMapper saleOrderMapper;
    private final PartnerMapper partnerMapper;

    private final PartnerFeignClient partnerFeignClient;
    private final SaleOrderSearchRepository saleOrderSearchRepository;

    @EventListener
    public void handlePurchaseOrderCreated(PurchaseOrderCreatedEvent event) {
        List<SaleOrder> saleOrders = saleOrderRepository.findByProjectIdAndStatus(event.getProjectId(), OrderStatus.NEW.name());
        for (SaleOrder saleOrder : saleOrders) {
            saleOrder.setStatus(OrderStatus.PROCESSING);
            saleOrderRepository.save(saleOrder);
        }
    }

    @Override
    public SaleOrderResponseDto createSaleOrder(SaleOrderRequestDto dto) {
        SaleOrder saleOrder = saleOrderMapper.toSaleOrder(dto);

        PartnerResponseDto partnerResponse = partnerFeignClient.getPartnerById(dto.partnerId());
        PartnerProjectResponseDto projectResponse = partnerFeignClient.getPartnerProject(
                dto.partnerId(), dto.projectId());

        Partner partnerSnapshot = partnerMapper.toPartnerSnapshot(partnerResponse);
        PartnerProject projectSnapshot = partnerMapper.toProjectSnapshot(projectResponse);

        saleOrder.setTotalAmount(new BigDecimal(0));
        saleOrder.setExportCode(CodeGenerator.generateCode(EntityType.EXPORT));
        saleOrder.setStatus(OrderStatus.NEW);
        saleOrder.setPartner(partnerSnapshot);
        saleOrder.setProject(projectSnapshot);

        SaleOrder savedSaleOrder = saleOrderRepository.save(saleOrder);

        return mapToResponseDtoWithSnapshots(savedSaleOrder);
    }

    @Override
    public SaleOrderResponseDto updateSaleOrder(Long id, UpdateSaleOrderRequestDto dto) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        if (saleOrder.getStatus() == OrderStatus.DONE || saleOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("PurchaseOrder with status " + saleOrder.getStatus() + " cannot be updated.");
        }

        saleOrderMapper.updateSaleOrderFromDto(dto, saleOrder);
        SaleOrder updated = saleOrderRepository.save(saleOrder);

        return mapToResponseDtoWithSnapshots(updated);
    }

    @Override
    public SaleOrderResponseDto getSaleOrderById(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        return mapToResponseDtoWithSnapshots(saleOrder);
    }

    @Override
    public void deleteSaleOrderById(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        SaleOrderDocument saleOrderDocument = saleOrderSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        saleOrderRepository.delete(saleOrder);
        saleOrderSearchRepository.delete(saleOrderDocument);
    }

    @Override
    public PagingResponse<GetAllSaleOrderResponseDto> getAllSaleOrders(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted, String searchKeyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<SaleOrder> pages;

        if (StringUtils.hasText(searchKeyword)) {
            pages = saleOrderRepository.findByDeletedAndExportCodeContainingIgnoreCase(deleted, searchKeyword, pageable);
        } else {
            pages = saleOrderRepository.findAllByDeleted(deleted, pageable);
        }

        List<SaleOrder> saleOrders = pages.getContent();

        if (CollectionUtils.isEmpty(saleOrders)) {
            return new PagingResponse<>(Collections.emptyList(), pages.getNumber(), pages.getSize(),
                    pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        }

        // GET PARTNER
        List<GetAllSaleOrderResponseDto> content = saleOrders.stream()
                .map(saleOrderMapper::toGetAllSaleOrderResponseDto)
                .collect(Collectors.toList());

        PagingResponse<GetAllSaleOrderResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> suggestSales(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = saleOrderSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(SaleOrderDocument::getExportCode)
                .distinct()
                .toList();
    }

    @Override
    public SaleOrderResponseDto cancelSaleOrder(Long id, String cancellationReason) {
        SaleOrder order = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Order", "id", id));

        return executeCancelOrder(order, StringUtils.hasText(cancellationReason) ? cancellationReason : "Cancelled by user request.");
    }

    @Override
    public void checkAndUpdateDoneStatus(SaleOrder so) {
        SaleOrder order = saleOrderRepository.findById(so.getId())
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", so.getId()));

        if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }

        boolean allDelivered = order.getDeliveryOrders() != null &&
                !order.getDeliveryOrders().isEmpty() &&
                order.getDeliveryOrders().stream()
                        .allMatch(deliveryOrder ->
                                deliveryOrder.getStatus().equals(OrderStatus.DELIVERED) ||
                                deliveryOrder.getStatus().equals(OrderStatus.DONE));

        boolean noDebtsOrAllPaid = saleDebtRepository.countNonPaidNonCancelledDebtsBySaleOrderId(order.getId()) == 0;

        if (allDelivered && noDebtsOrAllPaid && order.getStatus() == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.DONE);
            order.setSaleOrdersNote((StringUtils.hasText(order.getSaleOrdersNote()) ?
                    order.getSaleOrdersNote() + " | " : "") + "Completed on " + Instant.now());
            saleOrderRepository.save(order);
            saleOrderSearchRepository.findById(order.getId()).ifPresent(doc -> {
                doc.setStatus(order.getStatus().name());
                doc.setSaleOrdersNote(order.getSaleOrdersNote());
                saleOrderSearchRepository.save(doc);
            });
        }
    }


    @Override
    public void softDeleteSaleOrder(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale Order", "id", id));

        // Không cho soft-delete nếu đơn hàng đã hoàn thành hoặc đã bị hủy
        if (saleOrder.getStatus() == OrderStatus.DONE
                || saleOrder.getStatus() == OrderStatus.CANCELLED
                || saleOrder.getStatus() == OrderStatus.IN_TRANSIT
                || saleOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot delete a completed, in transit, delivered or cancelled order.");
        }

        saleOrder.setDeleted(true);
        saleOrderRepository.save(saleOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        saleOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(true);
            saleOrderSearchRepository.save(doc);
        });
    }

    @Override
    public SaleOrderResponseDto restoreSaleOrder(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale Order", "id", id));

        saleOrder.setDeleted(false);
        SaleOrder restoredOrder = saleOrderRepository.save(saleOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        saleOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(false);
            saleOrderSearchRepository.save(doc);
        });

        return mapToResponseDtoWithSnapshots(restoredOrder);
    }

    // --- Hàm private helper để cancel order ---
    private SaleOrderResponseDto executeCancelOrder(SaleOrder order, String reason) {
        // 1. Kiểm tra xem đơn hàng có thể hủy được không
        if (!canBeCancelled(order)) {
            throw new IllegalStateException("Sale Order with status " + order.getStatus() +
                    " cannot be cancelled.");
        }

        // 2. Cập nhật trạng thái đơn hàng
        order.setStatus(OrderStatus.CANCELLED);
        // thêm lý do hủy vào purchaseOrdersNote hoặc một trường mới
        String existingNote = order.getSaleOrdersNote();
        String cancellationNote = "Cancelled: " + reason;
        order.setSaleOrdersNote(StringUtils.hasText(existingNote) ? existingNote + " | " + cancellationNote : cancellationNote);

        // 3. Xử lý các logic nghiệp vụ liên quan đến hủy đơn
        if (order.getSaleDebts() != null) {
            order.getSaleDebts().forEach(debt -> {
                // nếu nợ chưa trả thì hủy, nếu đã trả một phần thì xem xét hoàn tiền

                if (debt.getStatus() == DebtStatus.UNPAID) {
                    debt.setStatus(DebtStatus.CANCELLED);
                    debt.setDebtNote((StringUtils.hasText(debt.getDebtNote()) ? debt.getDebtNote() + " | " : "") + "Order Cancelled");
                }
            });
        }
        // - Thông báo cho nhà cung cấp (nếu PO đã được gửi)
        // - Hoàn lại hàng vào kho (nếu đã xuất kho cho PO này - ít xảy ra với PO)
        // - Cập nhật Elasticsearch

        SaleOrder cancelledOrder = saleOrderRepository.save(order);

        // Cập nhật document trong Elasticsearch
        saleOrderSearchRepository.findById(cancelledOrder.getId()).ifPresent(doc -> {
            doc.setStatus(cancelledOrder.getStatus().name());
            saleOrderSearchRepository.save(doc);
        });

        SaleOrderResponseDto res = saleOrderMapper.toSaleOrderResponseDto(cancelledOrder);
        // Đảm bảo response DTO cũng phản ánh đúng thông tin snapshot
        if (cancelledOrder.getPartner() != null) {
            res.setPartner(partnerMapper.toPartnerResponseDto(cancelledOrder.getPartner()));
        }
        if (cancelledOrder.getProject() != null) {
            res.setProject(partnerMapper.toProjectResponseDto(cancelledOrder.getProject()));
        }
        return res;
    }

    // --- Hàm private helper để kiểm tra điều kiện hủy ---
    private boolean canBeCancelled(SaleOrder order) {

        return order.getStatus().equals(OrderStatus.NEW); // Luôn có thể hủy khi đang là NEW
    }

    private SaleOrderResponseDto mapToResponseDtoWithSnapshots(SaleOrder so) {
        SaleOrderResponseDto res = saleOrderMapper.toSaleOrderResponseDto(so);
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponseDto(so.getPartner());
        PartnerProjectResponseDto partnerProjectResponseDto = partnerMapper.toProjectResponseDto(so.getProject());

        res.setPartner(partnerResponseDto);
        res.setProject(partnerProjectResponseDto);

        return res;
    }
}
