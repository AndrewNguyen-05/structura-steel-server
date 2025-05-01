package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.coreservice.entity.User;
import com.structura.steel.coreservice.mapper.PurchaseOrderMapper;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
import com.structura.steel.coreservice.repository.UserRepository;
import com.structura.steel.coreservice.service.PurchaseOrderService;
import com.structura.steel.dto.request.PurchaseOrderRequestDto;
import com.structura.steel.dto.response.GetAllPurchaseOrderResponseDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.dto.response.PurchaseOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.userId()));
        purchaseOrder.setUser(user);

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);

        PartnerProjectResponseDto partnerProject = partnerFeignClient.getPartnerProject(
                purchaseOrder.getSupplierId(), purchaseOrder.getProjectId());

        PurchaseOrderResponseDto res = purchaseOrderMapper.toPurchaseOrderResponseDto(saved);
        res.setProject(partnerProject);
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
}
