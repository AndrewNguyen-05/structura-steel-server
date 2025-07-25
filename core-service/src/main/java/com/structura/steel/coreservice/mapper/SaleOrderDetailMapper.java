package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.sale.GetAllSaleOrderDetailResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderDetailResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface SaleOrderDetailMapper {

    SaleOrderDetail toSaleOrderDetail(SaleOrderDetailRequestDto dto);

    @Mapping(source = "detail.product.id", target = "productId")
    @Mapping(source = "saleOrder.id", target = "saleOrderId")
    SaleOrderDetailResponseDto toSaleOrderDetailResponseDto(SaleOrderDetail detail);

    void updateSaleOrderDetailFromDto(SaleOrderDetailRequestDto dto, @MappingTarget SaleOrderDetail saleOrderDetail);

    List<SaleOrderDetailResponseDto> toSaleOrderDetailResponseDtoList(List<SaleOrderDetail> saleOrderDetails);

    GetAllSaleOrderDetailResponseDto toSaleOrderDetailGetAllDto(SaleOrderDetail saleOrderDetail);
}

