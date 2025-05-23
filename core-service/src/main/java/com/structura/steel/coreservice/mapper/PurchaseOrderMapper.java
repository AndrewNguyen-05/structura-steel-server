package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.elasticsearch.document.PurchaseOrderDocument;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.commons.dto.core.request.PurchaseOrderRequestDto;
import com.structura.steel.commons.dto.core.response.GetAllPurchaseOrderResponseDto;
import com.structura.steel.commons.dto.core.response.PurchaseOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {PurchaseOrderDetailMapper.class, PurchaseDebtMapper.class})
public interface PurchaseOrderMapper {

    PurchaseOrder toPurchaseOrder(PurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto toPurchaseOrderResponseDto(PurchaseOrder order);

    void updatePurchaseOrderFromDto(PurchaseOrderRequestDto dto,
                                    @MappingTarget PurchaseOrder order);

    GetAllPurchaseOrderResponseDto toGetAllPurchaseOrderResponseDto(PurchaseOrder order);

    List<PurchaseOrderResponseDto> toPurchaseOrderResponseDtoList(List<PurchaseOrder> orders);

    // --- Elasticsearch Document related mappings ---
    GetAllPurchaseOrderResponseDto toResponseDtoFromDocument(PurchaseOrderDocument purchaseOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    PurchaseOrderDocument toDocument(PurchaseOrder entity);

    PurchaseOrder fromDocument(PurchaseOrderDocument doc);
}
