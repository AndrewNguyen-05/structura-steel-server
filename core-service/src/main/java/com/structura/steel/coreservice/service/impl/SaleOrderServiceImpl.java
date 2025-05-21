package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.enumeration.EntityType;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.client.PartnerFeignClient;
import com.structura.steel.commons.utils.CodeGenerator;
import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.SaleOrderSearchRepository;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.User;
import com.structura.steel.coreservice.mapper.SaleOrderMapper;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
import com.structura.steel.coreservice.repository.UserRepository;
import com.structura.steel.coreservice.service.SaleOrderService;
import com.structura.steel.dto.request.SaleOrderRequestDto;
import com.structura.steel.dto.response.GetAllSaleOrderResponseDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.dto.response.SaleOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;

    private final SaleOrderMapper saleOrderMapper;

    private final PartnerFeignClient partnerFeignClient;
    private final SaleOrderSearchRepository saleOrderSearchRepository;

    @Override
    public SaleOrderResponseDto createSaleOrder(SaleOrderRequestDto dto) {
        SaleOrder saleOrder = saleOrderMapper.toSaleOrder(dto);

        PartnerResponseDto partnerResponse = partnerFeignClient.getPartnerById(saleOrder.getPartnerId());

        if (!saleOrder.getPartnerId().equals(partnerResponse.id())) {
            throw new ResourceNotBelongToException("Partner's project", "id", partnerResponse.id(), "partner", "id", partnerResponse.id());
        }

        saleOrder.setTotalAmount(new BigDecimal(0));
        saleOrder.setExportCode(CodeGenerator.generateCode(EntityType.EXPORT));

        SaleOrder savedSaleOrder = saleOrderRepository.save(saleOrder);

        PartnerProjectResponseDto partnerProjectResponse = partnerFeignClient.getPartnerProject(
                saleOrder.getPartnerId(),
                saleOrder.getProjectId()
        );

        SaleOrderResponseDto responseDto = saleOrderMapper.toSaleOrderResponseDto(savedSaleOrder);
        responseDto.setPartner(partnerResponse);
        responseDto.setProject(partnerProjectResponse);

        return responseDto;
    }

    @Override
    public SaleOrderResponseDto updateSaleOrder(Long id, SaleOrderRequestDto dto) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        PartnerResponseDto partnerResponse = partnerFeignClient.getPartnerById(saleOrder.getPartnerId());

        if (!saleOrder.getPartnerId().equals(partnerResponse.id())) {
            throw new ResourceNotBelongToException("Partner's project", "id", partnerResponse.id(), "partner", "id", partnerResponse.id());
        }

        saleOrderMapper.updateSaleOrderFromDto(dto, saleOrder);

        SaleOrder updated = saleOrderRepository.save(saleOrder);

        PartnerProjectResponseDto partnerProjectResponse = partnerFeignClient.getPartnerProject(
                saleOrder.getPartnerId(),
                saleOrder.getProjectId()
        );

        SaleOrderResponseDto responseDto = saleOrderMapper.toSaleOrderResponseDto(updated);
        responseDto.setPartner(partnerResponse);
        responseDto.setProject(partnerProjectResponse);

        return responseDto;
    }

    @Override
    public SaleOrderResponseDto getSaleOrderById(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));

        if(saleOrder.getPartnerId() == null || saleOrder.getProjectId() == null) {
            throw new RuntimeException("SaleOrder does not have a partner or project");
        }

        PartnerResponseDto partnerResponse = partnerFeignClient.getPartnerById(saleOrder.getPartnerId());

        if (!saleOrder.getPartnerId().equals(partnerResponse.id())) {
            throw new ResourceNotBelongToException("Partner's project", "id", partnerResponse.id(), "partner", "id", partnerResponse.id());
        }

        PartnerProjectResponseDto partnerProjectResponse = partnerFeignClient.getPartnerProject(
                saleOrder.getPartnerId(),
                saleOrder.getProjectId()
        );
//        UserResponse userResponse = userRepository.getUsersById(saleOrder.getUser());

        SaleOrderResponseDto responseDto = saleOrderMapper.toSaleOrderResponseDto(saleOrder);
        responseDto.setPartner(partnerResponse);
        responseDto.setProject(partnerProjectResponse);

        return responseDto;
    }

    @Override
    public void deleteSaleOrderById(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrder", "id", id));
        saleOrderRepository.delete(saleOrder);
    }

    @Override
    public PagingResponse<GetAllSaleOrderResponseDto> getAllSaleOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SaleOrder> pages = saleOrderRepository.findAll(pageable);
        List<SaleOrder> saleOrders = pages.getContent();

        if (CollectionUtils.isEmpty(saleOrders)) {
            return new PagingResponse<>(Collections.emptyList(), pages.getNumber(), pages.getSize(),
                    pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
        }

        // GET PARTNER
        List<GetAllSaleOrderResponseDto> content = saleOrders.stream()
                .map(saleOrderMapper::toGetAllSaleOrderResponseDto)
                .collect(Collectors.toList());

        List<Long> partnerIds = content.stream()
                .map(GetAllSaleOrderResponseDto::getPartnerId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!partnerIds.isEmpty()) {

            List<PartnerResponseDto> partners = partnerFeignClient.getPartnersByIds(partnerIds);

            Map<Long, String> partnerNameMap = partners.stream()
                    .collect(Collectors.toMap(
                            PartnerResponseDto::id,
                            PartnerResponseDto::partnerName
                    ));

            content.forEach(dto -> {
                String name = partnerNameMap.get(dto.getPartnerId());
                dto.setPartnerName(name);
            });
        }

        //GET PROJECT (tuong duong phia tren)
        List<Long> projectIds = content.stream()
                .map(GetAllSaleOrderResponseDto::getProjectId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!projectIds.isEmpty()) {
            Long partnerId = content.get(0).getPartnerId();
            List<PartnerProjectResponseDto> projects = partnerFeignClient
                    .getProjectsBatchByIds(partnerId, projectIds);

            Map<Long, String> projectNameMap = projects.stream()
                    .collect(Collectors.toMap(
                            PartnerProjectResponseDto::getId,
                            PartnerProjectResponseDto::getProjectName
                    ));

            content.forEach(dto ->
                    dto.setProjectName(projectNameMap.get(dto.getProjectId()))
            );
        }

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
}
