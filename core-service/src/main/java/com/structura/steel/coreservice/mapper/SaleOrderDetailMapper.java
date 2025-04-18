package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.dto.request.SaleOrderDetailRequestDto;
import com.structura.steel.dto.response.SaleOrderDetailResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface SaleOrderDetailMapper {

    SaleOrderDetail toSaleOrderDetail(SaleOrderDetailRequestDto dto);

    SaleOrderDetailResponseDto toSaleOrderDetailResponseDto(SaleOrderDetail saleOrderDetail);

    void updateSaleOrderDetailFromDto(SaleOrderDetailRequestDto dto, @MappingTarget SaleOrderDetail saleOrderDetail);

    List<SaleOrderDetailResponseDto> toSaleOrderDetailResponseDtoList(List<SaleOrderDetail> saleOrderDetails);
}

