package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.commons.dto.core.request.SaleOrderRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllSaleOrderResponseDto;
import com.structura.steel.commons.dto.core.response.SaleOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {SaleOrderDetailMapper.class, SaleDebtMapper.class})
public interface SaleOrderMapper {

    SaleOrder toSaleOrder(SaleOrderRequestDto dto);

    SaleOrderResponseDto toSaleOrderResponseDto(SaleOrder saleOrder);

    void updateSaleOrderFromDto(SaleOrderRequestDto dto, @MappingTarget SaleOrder saleOrder);

    GetAllSaleOrderResponseDto toGetAllSaleOrderResponseDto(SaleOrder saleOrder);

    List<SaleOrderResponseDto> toSaleOrderResponseDtoList(List<SaleOrder> saleOrders);

    // --- Elasticsearch Document related mappings ---
    GetAllSaleOrderResponseDto toResponseDtoFromDocument(SaleOrderDocument saleOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    SaleOrderDocument toDocument(SaleOrder entity);

    SaleOrder fromDocument(SaleOrderDocument doc);
}
