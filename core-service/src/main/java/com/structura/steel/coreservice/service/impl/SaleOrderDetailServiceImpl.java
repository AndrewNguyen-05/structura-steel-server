package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.mapper.SaleOrderDetailMapper;
import com.structura.steel.coreservice.repository.SaleOrderDetailRepository;
import com.structura.steel.coreservice.service.SaleOrderDetailService;
import com.structura.steel.dto.request.SaleOrderDetailRequestDto;
import com.structura.steel.dto.response.SaleOrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleOrderDetailServiceImpl implements SaleOrderDetailService {

    private final SaleOrderDetailRepository saleOrderDetailRepository;
    private final SaleOrderDetailMapper saleOrderDetailMapper;

    @Override
    public SaleOrderDetailResponseDto createSaleOrderDetail(SaleOrderDetailRequestDto dto, Long saleId) {
        SaleOrderDetail saleOrderDetail = saleOrderDetailMapper.toSaleOrderDetail(dto);
        SaleOrderDetail savedSaleOrderDetail = saleOrderDetailRepository.save(saleOrderDetail);
        return saleOrderDetailMapper.toSaleOrderDetailResponseDto(savedSaleOrderDetail);
    }

    @Override
    public SaleOrderDetailResponseDto updateSaleOrderDetail(Long id, SaleOrderDetailRequestDto dto, Long saleId) {
        SaleOrderDetail existing = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));
        saleOrderDetailMapper.updateSaleOrderDetailFromDto(dto, existing);
        SaleOrderDetail updated = saleOrderDetailRepository.save(existing);
        return saleOrderDetailMapper.toSaleOrderDetailResponseDto(updated);
    }

    @Override
    public SaleOrderDetailResponseDto getSaleOrderDetailById(Long id, Long saleId) {
        SaleOrderDetail saleOrderDetail = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));
        return saleOrderDetailMapper.toSaleOrderDetailResponseDto(saleOrderDetail);
    }

    @Override
    public void deleteSaleOrderDetailById(Long id, Long saleId) {
        SaleOrderDetail saleOrderDetail = saleOrderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleOrderDetail", "id", id));
        saleOrderDetailRepository.delete(saleOrderDetail);
    }

    @Override
    public PagingResponse<SaleOrderDetailResponseDto> getAllSaleOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir, Long saleId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SaleOrderDetail> pages = saleOrderDetailRepository.findAll(pageable);
        List<SaleOrderDetail> saleOrderDetails = pages.getContent();
        List<SaleOrderDetailResponseDto> content = saleOrderDetails.stream()
                .map(saleOrderDetailMapper::toSaleOrderDetailResponseDto)
                .collect(Collectors.toList());
        PagingResponse<SaleOrderDetailResponseDto> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());
        return response;
    }
}
