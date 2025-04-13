package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotBelongToException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.client.PartnerFeignClient;
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
import com.structura.steel.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;

    private final SaleOrderMapper saleOrderMapper;

    private final PartnerFeignClient partnerFeignClient;

    @Override
    public SaleOrderResponseDto createSaleOrder(SaleOrderRequestDto dto) {
        SaleOrder saleOrder = saleOrderMapper.toSaleOrder(dto);

        PartnerResponseDto partnerResponse = partnerFeignClient.getPartnerById(saleOrder.getPartnerId());

        if (!saleOrder.getPartnerId().equals(partnerResponse.id())) {
            throw new ResourceNotBelongToException("Partner's project", "id", partnerResponse.id(), "partner", "id", partnerResponse.id());
        }

        User user = userRepository.findById(dto.userId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.userId()));
        saleOrder.setUser(user);

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
}
