package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.sale.UpdateSaleOrderRequestDto;
import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.commons.dto.core.request.sale.SaleOrderRequestDto;
import com.structura.steel.commons.dto.core.response.sale.GetAllSaleOrderResponseDto;
import com.structura.steel.commons.dto.core.response.sale.SaleOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {SaleOrderDetailMapper.class, SaleDebtMapper.class})
public interface SaleOrderMapper {

    SaleOrder toSaleOrder(SaleOrderRequestDto dto);

    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    SaleOrderResponseDto toSaleOrderResponseDto(SaleOrder order);

    void updateSaleOrderFromDto(UpdateSaleOrderRequestDto dto, @MappingTarget SaleOrder saleOrder);

    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    GetAllSaleOrderResponseDto toGetAllSaleOrderResponseDto(SaleOrder order);

    List<SaleOrderResponseDto> toSaleOrderResponseDtoList(List<SaleOrder> saleOrders);

    // --- Elasticsearch Document related mappings ---
    GetAllSaleOrderResponseDto toResponseDtoFromDocument(SaleOrderDocument saleOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    SaleOrderDocument toDocument(SaleOrder entity);

    SaleOrder fromDocument(SaleOrderDocument doc);
}
