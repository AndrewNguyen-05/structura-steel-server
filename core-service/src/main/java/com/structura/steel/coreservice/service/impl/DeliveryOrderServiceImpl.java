package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.dto.core.request.delivery.UpdateDeliveryOrderRequestDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.dto.partner.response.VehicleResponseDto;
import com.structura.steel.commons.enumeration.ConfirmationStatus;
import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.commons.enumeration.DeliveryType;
import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.commons.exception.BadRequestException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.DeliveryOrderSearchRepository;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.Vehicle;
import com.structura.steel.coreservice.mapper.DeliveryOrderMapper;
import com.structura.steel.coreservice.mapper.PartnerMapper;
import com.structura.steel.coreservice.repository.DeliveryDebtRepository;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.service.DeliveryOrderService;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.delivery.GetAllDeliveryOrderResponseDto;
import com.structura.steel.coreservice.service.PurchaseOrderService;
import com.structura.steel.coreservice.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderSearchRepository deliveryOrderSearchRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final DeliveryDebtRepository deliveryDebtRepository;

    private final DeliveryOrderMapper deliveryOrderMapper;

    private final PartnerFeignClient partnerFeignClient;
    private final PartnerMapper partnerMapper;
    private final PurchaseOrderService purchaseOrderService;
    private final SaleOrderService saleOrderService;

    @Override
    public DeliveryOrderResponseDto createDeliveryOrder(DeliveryOrderRequestDto dto) {

        if (dto.purchaseOrderId() == null && dto.saleOrderId() == null) {
            throw new BadRequestException("Either purchaseOrderId or saleOrderId must be provided.");
        }
        if (dto.purchaseOrderId() != null && dto.saleOrderId() != null) {
            throw new BadRequestException("Only one of purchaseOrderId or saleOrderId should be provided.");
        }

        DeliveryOrder order = deliveryOrderMapper.toDeliveryOrder(dto);
        order.setTotalDeliveryFee(order.getDeliveryUnitPrice().add(order.getAdditionalFees()));

        VehicleResponseDto vehicleDto = partnerFeignClient.getVehicleByPartnerId(dto.partnerId(), dto.vehicleId());
        PartnerResponseDto partnerResponseDto = partnerFeignClient.getPartnerById(dto.partnerId());

        Vehicle vehicle = partnerMapper.toVehicleSnapshot(vehicleDto);
        Partner partner = partnerMapper.toPartnerSnapshot(partnerResponseDto);

        order.setDeliveryCode(CodeGenerator.generateCode(EntityType.DELIVERY));
        order.setStatus(OrderStatus.NEW);
        order.setVehicle(vehicle);
        order.setPartner(partner);
        order.setConfirmationFromFactory(ConfirmationStatus.PENDING);
        order.setConfirmationFromPartner(ConfirmationStatus.PENDING);
        order.setConfirmationFromReceiver(ConfirmationStatus.PENDING);

        // Xác định DeliveryType dựa trên orderId
        if (dto.purchaseOrderId() != null) {
            PurchaseOrder po = purchaseOrderRepository.findById(dto.purchaseOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", dto.purchaseOrderId()));
            order.setPurchaseOrder(po);
            order.setDeliveryType(DeliveryType.WAREHOUSE_IMPORT); // SUPPLIER
        } else {
            SaleOrder so = saleOrderRepository.findById(dto.saleOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", dto.saleOrderId()));
            order.setSaleOrder(so);
            order.setDeliveryType(DeliveryType.PROJECT_DELIVERY); // CUSTOMER
        }

        DeliveryOrder saved = deliveryOrderRepository.save(order);
        return mapToResponseDtoWithSnapshots(saved);
    }

    @Override
    public DeliveryOrderResponseDto updateDeliveryOrder(Long id, UpdateDeliveryOrderRequestDto dto) {

        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));

        if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Delivery with status " + order.getStatus() + " cannot be updated.");
        }

        deliveryOrderMapper.updateDeliveryOrderFromDto(dto, order);
        order.setTotalDeliveryFee(order.getDeliveryUnitPrice().add(order.getAdditionalFees()));
        DeliveryOrder updated = new DeliveryOrder();

        if(order.getPurchaseOrder() != null) {
            // Check trạng thái, đầu tiên là delivered, nếu vậy thì gọi hàm check của purchase
            if(dto.confirmationFromReceiver().equals(ConfirmationStatus.YES) && order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.DELIVERED);
                /** update ngay tại đây để lúc check thì check cho purchase,
                 *  nếu không save ở dây, thì lúc purchase nó check bên dưới
                 *  nó thấy vẫn còn delivery chưa DELIVERED (do chưa lưu mà)
                 *  thì nó sẽ set là DELIVERED chứ khng phải DONE
                 */
                updated = deliveryOrderRepository.saveAndFlush(order);
                checkAndUpdateDoneStatus(order); // check xem có done hết nợ chưa
                PurchaseOrder po = order.getPurchaseOrder();
                po.setStatus(OrderStatus.DELIVERED);
                purchaseOrderRepository.save(po);
                purchaseOrderService.checkAndUpdateDoneStatus(po);

            } else if(dto.confirmationFromFactory().equals(ConfirmationStatus.YES) && order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.IN_TRANSIT);
                order.setConfirmationFromFactory(ConfirmationStatus.YES);
                updated = deliveryOrderRepository.saveAndFlush(order);
                PurchaseOrder po = order.getPurchaseOrder();
                po.setStatus(OrderStatus.IN_TRANSIT);
                purchaseOrderRepository.save(po);

            } else if(dto.confirmationFromFactory().equals(ConfirmationStatus.NO)) {
                return executeCancelOrder(order, "Delivery partner rejected the order.");
            }
        } else {
            if(dto.confirmationFromReceiver().equals(ConfirmationStatus.YES) && order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.DELIVERED);
                updated = deliveryOrderRepository.saveAndFlush(order);
                checkAndUpdateDoneStatus(order); // check xem có done hết nợ chưa
                SaleOrder so = order.getSaleOrder();
                so.setStatus(OrderStatus.DELIVERED);
                saleOrderRepository.save(so);
                saleOrderService.checkAndUpdateDoneStatus(so);

            } else if(dto.confirmationFromPartner().equals(ConfirmationStatus.YES) && order.getStatus() == OrderStatus.NEW) {
                order.setStatus(OrderStatus.IN_TRANSIT);
                order.setConfirmationFromPartner(ConfirmationStatus.YES);
                updated = deliveryOrderRepository.saveAndFlush(order);
                SaleOrder so = order.getSaleOrder();
                so.setStatus(OrderStatus.IN_TRANSIT);
                saleOrderRepository.save(so);

            } else if(dto.confirmationFromPartner().equals(ConfirmationStatus.NO)) {
                return executeCancelOrder(order, "Delivery partner rejected the order.");
            }
        }


        return mapToResponseDtoWithSnapshots(updated);
    }

    @Override
    public DeliveryOrderResponseDto getDeliveryOrderById(Long id) {
        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));
        return mapToResponseDtoWithSnapshots(order);
    }

    @Override
    public void deleteDeliveryOrderById(Long id) {
        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));

        DeliveryOrderDocument document = deliveryOrderSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryOrder", "id", id));

        deliveryOrderRepository.delete(order);
        deliveryOrderSearchRepository.delete(document);
    }

    @Override
    public PagingResponse<GetAllDeliveryOrderResponseDto> getAllDeliveryOrders(int pageNo, int pageSize, String sortBy, String sortDir, boolean deleted, String searchKeyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<DeliveryOrder> pages;

        if (StringUtils.hasText(searchKeyword)) {
            pages = deliveryOrderRepository.findByDeletedAndDeliveryCodeContainingIgnoreCase(deleted, searchKeyword, pageable);
        } else {
            pages = deliveryOrderRepository.findAllByDeleted(deleted, pageable);
        }

        List<GetAllDeliveryOrderResponseDto> content = pages.getContent().stream()
                .map(deliveryOrderMapper::toGetAllDeliveryOrderResponseDto)
                .collect(Collectors.toList());

        PagingResponse<GetAllDeliveryOrderResponseDto> response = new PagingResponse<>();
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
    public List<String> suggestDeliveries(String prefix, int size) {
        if (!StringUtils.hasText(prefix)) {
            return Collections.emptyList();
        }
        // Gọi thẳng repository, nó sẽ tìm prefix trên sub‐field _index_prefix
        var page = deliveryOrderSearchRepository.findBySuggestionPrefix(prefix, PageRequest.of(0, size));
        return page.getContent().stream()
                .map(DeliveryOrderDocument::getDeliveryCode)
                .distinct()
                .toList();
    }

    @Override
    public DeliveryOrderResponseDto cancelDeliveryOrder(Long id, String cancellationReason) {
        DeliveryOrder order = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Order", "id", id));

        return executeCancelOrder(order, StringUtils.hasText(cancellationReason) ? cancellationReason : "Cancelled by user request.");
    }

    @Override
    public void checkAndUpdateDoneStatus(DeliveryOrder order) {
        if (order.getStatus()== OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELLED) {
            return; // Đã DONE hoặc CANCELLED, không cần kiểm tra
        }

        // Kiểm tra tất cả debt
        boolean noDebtsOrAllPaid = deliveryDebtRepository.countNonPaidNonCancelledDebtsByDeliveryOrderId(order.getId()) == 0;

        // Nếu tất cả DeliveryOrder DELIVERED và không có nợ hoặc tất cả nợ PAID
        if (noDebtsOrAllPaid && order.getStatus() == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.DONE);
            order.setDeliveryOrderNote((StringUtils.hasText(order.getDeliveryOrderNote()) ?
                    order.getDeliveryOrderNote() + " | " : "") + "Completed on " + Instant.now() +
                    " - All deliveries completed and debts paid.");
            deliveryOrderRepository.save(order);

            // Cập nhật Elasticsearch
            deliveryOrderSearchRepository.findById(order.getId()).ifPresent(doc -> {
                doc.setStatus(order.getStatus().name());
                doc.setDeliveryOrderNote(order.getDeliveryOrderNote());
                deliveryOrderSearchRepository.save(doc);
            });
            log.info("Delivery order {} updated to DONE - All deliveries completed and debts paid.", order.getId());
        }
    }

    @Override
    public void softDeleteDeliveryOrder(Long id) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Order", "id", id));

        // Không cho soft-delete nếu đơn hàng đã hoàn thành hoặc đã bị hủy
        if (deliveryOrder.getStatus() == OrderStatus.DONE
                || deliveryOrder.getStatus() == OrderStatus.CANCELLED
                || deliveryOrder.getStatus() == OrderStatus.IN_TRANSIT
                || deliveryOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot delete a completed, in transit, delivered or cancelled order.");
        }

        deliveryOrder.setDeleted(true);
        deliveryOrderRepository.save(deliveryOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        deliveryOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(true);
            deliveryOrderSearchRepository.save(doc);
        });
    }

    @Override
    public DeliveryOrderResponseDto restoreDeliveryOrder(Long id) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Order", "id", id));

        deliveryOrder.setDeleted(false);
        DeliveryOrder restoredOrder = deliveryOrderRepository.save(deliveryOrder);

        // Đồng bộ trạng thái deleted sang Elasticsearch
        deliveryOrderSearchRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(false);
            deliveryOrderSearchRepository.save(doc);
        });

        return mapToResponseDtoWithSnapshots(restoredOrder);
    }


    private DeliveryOrderResponseDto executeCancelOrder(DeliveryOrder order, String reason) {
        if (!canBeCancelled(order)) {
            throw new BadRequestException("DeliveryOrder with status " + order.getStatus() + " cannot be cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        String existingNote = order.getDeliveryOrderNote();
        String cancellationNote = "Cancelled: " + reason;
        order.setDeliveryOrderNote(StringUtils.hasText(existingNote) ? existingNote + " | " + cancellationNote : cancellationNote);

        // Hủy các DeliveryDebt liên quan
        if (order.getDeliveryDebts() != null) {
            order.getDeliveryDebts().forEach(debt -> {
                if (debt.getStatus() == DebtStatus.UNPAID) {
                    debt.setStatus(DebtStatus.CANCELLED);
                    debt.setDebtNote((StringUtils.hasText(debt.getDebtNote()) ? debt.getDebtNote() + " | " : "") + "Order Cancelled");
                }
            });
        }

        DeliveryOrder cancelledOrder = deliveryOrderRepository.save(order);

        // Cập nhật Elasticsearch
        deliveryOrderSearchRepository.findById(cancelledOrder.getId()).ifPresent(doc -> {
            doc.setStatus(cancelledOrder.getStatus().name());
            doc.setDeliveryOrderNote(cancelledOrder.getDeliveryOrderNote());
            deliveryOrderSearchRepository.save(doc);
        });

        return deliveryOrderMapper.toDeliveryOrderResponseDto(cancelledOrder);
    }

    // --- Hàm private helper để kiểm tra điều kiện hủy ---
    private boolean canBeCancelled(DeliveryOrder order) {

        return order.getStatus().equals(OrderStatus.NEW); // Luôn có thể hủy khi đang là NEW
    }

    private DeliveryOrderResponseDto mapToResponseDtoWithSnapshots(DeliveryOrder order) {
        DeliveryOrderResponseDto res = deliveryOrderMapper.toDeliveryOrderResponseDto(order);

        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponseDto(order.getPartner());
        VehicleResponseDto vehicleResponseDto = partnerMapper.toVehicleResponseDto(order.getVehicle());

        res.setPartner(partnerResponseDto);
        res.setVehicle(vehicleResponseDto);

        return res;
    }
}
