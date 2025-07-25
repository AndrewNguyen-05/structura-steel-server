package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderRequestDto;
import com.structura.steel.commons.enumeration.ConfirmationStatus;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.elasticsearch.document.PurchaseOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.PurchaseOrderSearchRepository;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.PartnerProject;
import com.structura.steel.coreservice.event.PurchaseOrderCreatedEvent;
import com.structura.steel.coreservice.mapper.PartnerMapper;
import com.structura.steel.coreservice.mapper.PurchaseOrderMapper;
import com.structura.steel.coreservice.repository.PurchaseDebtRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.PurchaseOrderService;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.GetAllPurchaseOrderResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
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
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseDebtRepository purchaseDebtRepository;

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PartnerMapper partnerMapper;

    private final PartnerFeignClient partnerFeignClient;
    private final PurchaseOrderSearchRepository purchaseOrderSearchRepository;

    private final ApplicationEventPublisher eventPublisher;

    private static final PartnerProject INTERNAL_STOCK_PROJECT = new PartnerProject(
            -1L,
            null,
            "INTERNAL_STOCK",
            "Đơn hàng nhập kho nội bộ",
            "Structura Steel",
            "Anh Nguyen",
            "0985274643",
            "Anh Nguyen",
            "0985274643",
            "Structura Steel",
            (short) 1,
            Instant.now(),
            Instant.now(),
            "System",
            "System"
    );

    @Override
    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto) {

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toPurchaseOrder(dto);

        PartnerResponseDto supplierResponse = partnerFeignClient.getPartnerById(dto.supplierId());
        Partner supplierSnapshot = partnerMapper.toPartnerSnapshot(supplierResponse);

        if(dto.saleOrderId() == null) {
            purchaseOrder.setProject(INTERNAL_STOCK_PROJECT);

        } else {
            SaleOrder so = saleOrderRepository.findById(dto.saleOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", dto.saleOrderId()));

            // kiểm tra trạng thái SaleOrder
            if (so.getStatus() == OrderStatus.CANCELLED) {
                throw new BadRequestException("Cannot create PurchaseOrder for a cancelled SaleOrder with id " + dto.saleOrderId());
            }

            // kiểm tra đã có PurchaseOrder chưa
            if (so.getPurchaseOrder() != null) {
                throw new DuplicateKeyException("PurchaseOrder", "saleOrderId", dto.saleOrderId());
            }

            PartnerProjectResponseDto projectResponse = partnerFeignClient.getProjectById(so.getProject().id());
            PartnerProject projectSnapshot = partnerMapper.toProjectSnapshot(projectResponse);

            purchaseOrder.setProject(projectSnapshot);
            purchaseOrder.setSaleOrder(so);
        }

        purchaseOrder.setTotalAmount(new BigDecimal(0));
        purchaseOrder.setImportCode(CodeGenerator.generateCode(EntityType.IMPORT));
        purchaseOrder.setStatus(OrderStatus.NEW);
        purchaseOrder.setConfirmationFromSupplier(ConfirmationStatus.PENDING);
        purchaseOrder.setSupplier(supplierSnapshot);

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        eventPublisher.publishEvent(new PurchaseOrderCreatedEvent(this, saved.getProject().id()));

        return mapToResponseDtoWithSnapshots(saved);
    }

    @Override
    public PurchaseOrderResponseDto updatePurchaseOrder(Long id, UpdatePurchaseOrderRequestDto dto) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        if (po.getStatus() == OrderStatus.DONE || po.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("PurchaseOrder with status " + po.getStatus() + " cannot be updated.");
        }

        if(dto.confirmationFromSupplier().equals(ConfirmationStatus.YES)) {
            po.setStatus(OrderStatus.PROCESSING);
        } else if(dto.confirmationFromSupplier().equals(ConfirmationStatus.NO)) {
            // **TRIGGER HỦY ĐƠN HÀNG NẾU NCC TỪ CHỐI**

            // chỉ tự động hủy nếu đang là NEW hoặc PENDING_CONFIRMATION
            if (po.getStatus().equals(OrderStatus.NEW) || po.getStatus().equals(OrderStatus.PROCESSING)) { // Thêm các trạng thái phù hợp

                return executeCancelOrder(po, "Supplier rejected the order.");
            } else {
                // Nếu đơn hàng đang ở trạng thái khác (ví dụ PROCESSING đã được xác nhận YES trước đó)
                // mà NCC lại đổi thành NO, thì đây là trường hợp phức tạp cần xem xét nghiệp vụ.
                // Có thể không tự động hủy mà chuyển sang ON_HOLD hoặc yêu cầu xử lý thủ công.
                // po.setStatus(OrderStatus.ON_HOLD);
                // po.setPurchaseOrdersNote((StringUtils.hasText(po.getPurchaseOrdersNote()) ? po.getPurchaseOrdersNote() + " | " : "") + "Supplier confirmation changed to NO, requires review.");
            }
        }

        purchaseOrderMapper.updatePurchaseOrderFromDto(dto, po);
        PurchaseOrder updated = purchaseOrderRepository.save(po);

        return mapToResponseDtoWithSnapshots(updated);
    }

    @Override
    public PurchaseOrderResponseDto getPurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        return mapToResponseDtoWithSnapshots(po);
    }

    @Override
    public void deletePurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        if (!purchaseOrder.getDeliveryOrders().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete this purchase order with code " + purchaseOrder.getImportCode() + ". There are active delivery orders associated with this purchase order."
            );
        }

        PurchaseOrderDocument pd = purchaseOrderSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        purchaseOrderRepository.delete(purchaseOrder);
        purchaseOrderSearchRepository.delete(pd);
    }

    @Override
    public PagingResponse<GetAllPurchaseOrderResponseDto> getAllPurchaseOrders(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted, String searchKeyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PurchaseOrder> pages;

        if (StringUtils.hasText(searchKeyword)) {
            pages = purchaseOrderRepository.findByDeletedAndImportCodeContainingIgnoreCase(deleted, searchKeyword, pageable);
        } else {
            pages = purchaseOrderRepository.findAllByDeleted(deleted, pageable);
        }

        List<PurchaseOrder> purchaseOrders = pages.getContent();

        if (CollectionUtils.isEmpty(purchaseOrders)) {
            return new PagingResponse<>(Collections.emptyList(), pages.getNumber(), pages.getSize(),
                    pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        }

        List<GetAllPurchaseOrderResponseDto> content = pages.getContent().stream()
                .map(purchaseOrderMapper::toGetAllPurchaseOrderResponseDto)
                .collect(Collectors.toList());

        PagingResponse<GetAllPurchaseOrderResponseDto> response = new PagingResponse<>();
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
    public List<String> suggestPurchases(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = purchaseOrderSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(PurchaseOrderDocument::getImportCode)
                .distinct()
                .toList();
    }

    @Override
    public PurchaseOrderResponseDto cancelPurchaseOrder(Long id, String cancellationReason) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        return executeCancelOrder(po, StringUtils.hasText(cancellationReason) ? cancellationReason : "Cancelled by user request.");
    }

    @Override
    public void checkAndUpdateDoneStatus(PurchaseOrder po) {
        PurchaseOrder order = purchaseOrderRepository.findById(po.getId())
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", po.getId()));

        if (po.getStatus() == OrderStatus.DONE || po.getStatus() == OrderStatus.CANCELLED) {
            return; // Đã DONE hoặc CANCELLED, không cần kiểm tra
        }

        // Kiểm tra tất cả DeliveryOrder
        boolean allDelivered = po.getDeliveryOrders() != null &&
                !po.getDeliveryOrders().isEmpty() &&
                po.getDeliveryOrders().stream()
                        .allMatch(deliveryOrder ->
                                deliveryOrder.getStatus().equals(OrderStatus.DELIVERED) ||
                                        deliveryOrder.getStatus().equals(OrderStatus.DONE));

        // Kiểm tra tất cả PurchaseDebt
        boolean noDebtsOrAllPaid = purchaseDebtRepository.countNonPaidNonCancelledDebtsByPurchaseOrderId(po.getId()) == 0;

        // Nếu tất cả DeliveryOrder DELIVERED và không có nợ hoặc tất cả nợ PAID
        if (allDelivered && noDebtsOrAllPaid && po.getStatus() == OrderStatus.DELIVERED) {
            po.setStatus(OrderStatus.DONE);
            po.setPurchaseOrdersNote((StringUtils.hasText(po.getPurchaseOrdersNote()) ?
                    po.getPurchaseOrdersNote() + " | " : "") + "Completed on " + Instant.now() +
                    " - All deliveries completed and debts paid.");
            purchaseOrderRepository.save(po);

            // Cập nhật Elasticsearch
            purchaseOrderSearchRepository.findById(po.getId()).ifPresent(doc -> {
                doc.setStatus(po.getStatus().name());
                doc.setPurchaseOrdersNote(po.getPurchaseOrdersNote());
                purchaseOrderSearchRepository.save(doc);
            });
            log.info("PurchaseOrder {} updated to DONE - All deliveries completed and debts paid.", po.getId());
        }
    }

    @Override
    public void softDeletePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        // Không cho soft-delete nếu đơn hàng đã hoàn thành hoặc đã bị hủy
        if (purchaseOrder.getStatus() == OrderStatus.DONE
                || purchaseOrder.getStatus() == OrderStatus.CANCELLED
                || purchaseOrder.getStatus() == OrderStatus.IN_TRANSIT
                || purchaseOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot delete a completed, in transit, delivered or cancelled order.");
        }

        if (!purchaseOrder.getDeliveryOrders().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete this purchase order with code " + purchaseOrder.getImportCode() + ". There are active delivery orders associated with this purchase order."
            );
        }

        purchaseOrder.setDeleted(true);
        purchaseOrderRepository.save(purchaseOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        purchaseOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(true);
            purchaseOrderSearchRepository.save(doc);
        });
    }

    @Override
    public PurchaseOrderResponseDto restorePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        purchaseOrder.setDeleted(false);
        PurchaseOrder restoredOrder = purchaseOrderRepository.save(purchaseOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        purchaseOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(false);
            purchaseOrderSearchRepository.save(doc);
        });

        return mapToResponseDtoWithSnapshots(restoredOrder);
    }


    // --- Hàm private helper để cancel order ---
    private PurchaseOrderResponseDto executeCancelOrder(PurchaseOrder purchaseOrder, String reason) {
        // 1. Kiểm tra xem đơn hàng có thể hủy được không
        if (!canBeCancelled(purchaseOrder)) {
            throw new BadRequestException("PurchaseOrder with status " + purchaseOrder.getStatus() +
                    " and confirmation " + purchaseOrder.getConfirmationFromSupplier() +
                    " cannot be cancelled.");
        }

        // 2. Cập nhật trạng thái đơn hàng
        purchaseOrder.setStatus(OrderStatus.CANCELLED);
        purchaseOrder.setSaleOrder(null);
        // thêm lý do hủy vào purchaseOrdersNote hoặc một trường mới
        String existingNote = purchaseOrder.getPurchaseOrdersNote();
        String cancellationNote = "Cancelled: " + reason;
        purchaseOrder.setPurchaseOrdersNote(StringUtils.hasText(existingNote) ? existingNote + " | " + cancellationNote : cancellationNote);

        // 3. Xử lý các logic nghiệp vụ liên quan đến hủy đơn
        if (purchaseOrder.getPurchaseDebts() != null) {
            purchaseOrder.getPurchaseDebts().forEach(debt -> {
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

        PurchaseOrder cancelledOrder = purchaseOrderRepository.save(purchaseOrder);

        // Cập nhật document trong Elasticsearch
        purchaseOrderSearchRepository.findById(cancelledOrder.getId()).ifPresent(doc -> {
            doc.setStatus(cancelledOrder.getStatus().name());
            purchaseOrderSearchRepository.save(doc);
        });

        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(cancelledOrder);
        // Đảm bảo response DTO cũng phản ánh đúng thông tin snapshot
        if (cancelledOrder.getSupplier() != null) {
            res.setSupplier(partnerMapper.toPartnerResponseDto(cancelledOrder.getSupplier()));
        }
        if (cancelledOrder.getProject() != null) {
            res.setProject(partnerMapper.toProjectResponseDto(cancelledOrder.getProject()));
        }
        return res;
    }

    // --- Hàm private helper để kiểm tra điều kiện hủy ---
    private boolean canBeCancelled(PurchaseOrder order) {

        // - Có thể hủy nếu đang là NEW và nhà cung cấp chưa xác nhận (PENDING hoặc NO)
        // - Có thể hủy nếu đang là PROCESSING và nhà cung cấp chưa xác nhận (PENDING hoặc NO)
        // - KHÔNG THỂ hủy nếu đã DONE.
        // - Nếu đã CANCELLED rồi thì không hủy nữa.

        return order.getStatus().equals(OrderStatus.NEW); // Luôn có thể hủy khi đang là NEW
    }

    private PurchaseOrderResponseDto mapToResponseDtoWithSnapshots(PurchaseOrder po) {
        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(po);
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponseDto(po.getSupplier());
        PartnerProjectResponseDto partnerProjectResponseDto = partnerMapper.toProjectResponseDto(po.getProject());

        res.setSupplier(partnerResponseDto);
        res.setProject(partnerProjectResponseDto);

        return res;
    }

    private void updateElasticsearchDocumentForCancel(PurchaseOrder order) {
        purchaseOrderSearchRepository.findById(order.getId()).ifPresent(doc -> {
            doc.setStatus(order.getStatus().name()); // Hoặc .text() tùy field trong document
            // Cập nhật thêm các trường khác trong document nếu cần, ví dụ note
            doc.setPurchaseOrdersNote(order.getPurchaseOrdersNote());
            purchaseOrderSearchRepository.save(doc);
        });
    }
}
