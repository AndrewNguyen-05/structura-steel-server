package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.User;
import com.structura.steel.coreservice.mapper.PurchaseOrderMapper;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.repository.UserRepository;
import com.structura.steel.coreservice.service.PurchaseOrderService;
import com.structura.steel.dto.request.PurchaseOrderRequestDto;
import com.structura.steel.dto.response.GetAllPurchaseOrderResponseDto;
import com.structura.steel.dto.response.GetAllSaleOrderResponseDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.dto.response.PurchaseOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PartnerFeignClient partnerFeignClient;

    @Override
    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toPurchaseOrder(dto);

        PartnerResponseDto supplierResponse = partnerFeignClient.getPartnerById(purchaseOrder.getSupplierId());
        if (!purchaseOrder.getSupplierId().equals(supplierResponse.id())) {
            throw new ResourceNotBelongToException("Supplier's project", "id",
                    supplierResponse.id(), "supplier", "id", supplierResponse.id());
        }
        purchaseOrder.setTotalAmount(new BigDecimal(0));
        purchaseOrder.setImportCode(CodeGenerator.generateCode(EntityType.IMPORT));

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);

        PartnerProjectResponseDto partnerProject = partnerFeignClient.getPartnerProject(
                purchaseOrder.getSupplierId(), purchaseOrder.getProjectId());

        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(saved);
        res.setProject(partnerProject);
        res.setSupplier(supplierResponse);
        return res;
    }

    @Override
    public PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderRequestDto dto) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        PartnerResponseDto supplierResponse = partnerFeignClient.getPartnerById(po.getSupplierId());
        if (!po.getSupplierId().equals(supplierResponse.id())) {
            throw new ResourceNotBelongToException("Supplier's project", "id",
                    supplierResponse.id(), "supplier", "id", supplierResponse.id());
        }

        purchaseOrderMapper.updatePurchaseOrderFromDto(dto, po);
        PurchaseOrder updated = purchaseOrderRepository.save(po);

        PartnerProjectResponseDto partnerProject = partnerFeignClient.getPartnerProject(
                po.getSupplierId(), po.getProjectId());

        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(updated);
        res.setProject(partnerProject);
        return res;
    }

    @Override
    public PurchaseOrderResponseDto getPurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        PartnerResponseDto supplierResponse = partnerFeignClient.getPartnerById(po.getSupplierId());
        if (!po.getSupplierId().equals(supplierResponse.id())) {
            throw new ResourceNotBelongToException("Supplier's project", "id",
                    supplierResponse.id(), "supplier", "id", supplierResponse.id());
        }

        PartnerProjectResponseDto partnerProject = partnerFeignClient.getPartnerProject(
                po.getSupplierId(), po.getProjectId());

        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(po);
        res.setProject(partnerProject);
        return res;
    }

    @Override
    public void deletePurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));
        purchaseOrderRepository.delete(po);
    }

    @Override
    public PagingResponse<GetAllPurchaseOrderResponseDto> getAllPurchaseOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PurchaseOrder> pages = purchaseOrderRepository.findAll(pageable);
        List<PurchaseOrder> purchaseOrders = pages.getContent();

        if (CollectionUtils.isEmpty(purchaseOrders)) {
            return new PagingResponse<>(Collections.emptyList(), pages.getNumber(), pages.getSize(),
                    pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        }

        // GET PARTNER
        List<GetAllPurchaseOrderResponseDto> content = purchaseOrders.stream()
                .map(purchaseOrderMapper::toGetAllPurchaseOrderResponseDto)
                .collect(Collectors.toList());

        List<Long> supplierIds = content.stream()
                .map(GetAllPurchaseOrderResponseDto::getSupplierId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!supplierIds.isEmpty()) {

            List<PartnerResponseDto> partners = partnerFeignClient.getPartnersByIds(supplierIds);

            Map<Long, String> partnerNameMap = partners.stream()
                    .collect(Collectors.toMap(
                            PartnerResponseDto::id,
                            PartnerResponseDto::partnerName
                    ));

            content.forEach(dto -> {
                String name = partnerNameMap.get(dto.getSupplierId());
                dto.setSupplierName(name);
            });
        }

        //GET PROJECT (tuong duong phia tren)
        List<Long> projectIds = content.stream()
                .map(GetAllPurchaseOrderResponseDto::getProjectId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!projectIds.isEmpty()) {
            Long supplierId = content.get(0).getSupplierId();
            List<PartnerProjectResponseDto> projects = partnerFeignClient
                    .getProjectsBatchByIds(supplierId, projectIds);

            Map<Long, String> projectNameMap = projects.stream()
                    .collect(Collectors.toMap(
                            PartnerProjectResponseDto::getId,
                            PartnerProjectResponseDto::getProjectName
                    ));

            content.forEach(dto ->
                    dto.setProjectName(projectNameMap.get(dto.getProjectId()))
            );
        }

        PagingResponse<GetAllPurchaseOrderResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }
}
