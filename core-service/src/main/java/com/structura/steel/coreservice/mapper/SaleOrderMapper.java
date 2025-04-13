package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.dto.request.SaleOrderRequestDto;
import com.structura.steel.dto.response.GetAllSaleOrderResponseDto;
import com.structura.steel.dto.response.SaleOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {SaleOrderDetailMapper.class, SaleDebtMapper.class})
public interface SaleOrderMapper {

    @Mapping(target = "user", ignore = true) // Xử lý riêng hoặc mapping theo nhu cầu
    SaleOrder toSaleOrder(SaleOrderRequestDto dto);

    SaleOrderResponseDto toSaleOrderResponseDto(SaleOrder saleOrder);

    void updateSaleOrderFromDto(SaleOrderRequestDto dto, @MappingTarget SaleOrder saleOrder);

    GetAllSaleOrderResponseDto toGetAllSaleOrderResponseDto(SaleOrder saleOrder);

    List<SaleOrderResponseDto> toSaleOrderResponseDtoList(List<SaleOrder> saleOrders);
}
