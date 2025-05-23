package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.commons.dto.core.request.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.GetAllDeliveryOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {DeliveryDebtMapper.class})
public interface DeliveryOrderMapper {

    DeliveryOrder toDeliveryOrder(DeliveryOrderRequestDto dto);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "saleOrderId", source = "saleOrder.id")
    DeliveryOrderResponseDto toDeliveryOrderResponseDto(DeliveryOrder order);

    void updateDeliveryOrderFromDto(DeliveryOrderRequestDto dto,
                                    @MappingTarget DeliveryOrder order);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "saleOrderId", source = "saleOrder.id")
    GetAllDeliveryOrderResponseDto toGetAllDeliveryOrderResponseDto(DeliveryOrder order);

    List<DeliveryOrderResponseDto> toDeliveryOrderResponseDtoList(List<DeliveryOrder> orders);

    // --- Elasticsearch Document related mappings ---
    GetAllDeliveryOrderResponseDto toResponseDtoFromDocument(DeliveryOrderDocument deliveryOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    DeliveryOrderDocument toDocument(DeliveryOrder entity);

    DeliveryOrder fromDocument(DeliveryOrderDocument doc);
}
