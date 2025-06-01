package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.delivery.UpdateDeliveryOrderRequestDto;
import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryOrderResponseDto;
import com.structura.steel.commons.dto.core.response.delivery.GetAllDeliveryOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {DeliveryDebtMapper.class})
public interface DeliveryOrderMapper {

    DeliveryOrder toDeliveryOrder(DeliveryOrderRequestDto dto);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "saleOrderId", source = "saleOrder.id")
    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    @Mapping(target = "deliveryType", expression = "java(order.getDeliveryType().text())")
    @Mapping(target = "confirmationFromFactory", expression = "java(order.getConfirmationFromFactory() == null ? null : order.getConfirmationFromFactory().text())")
    @Mapping(target = "confirmationFromPartner", expression = "java(order.getConfirmationFromPartner() == null ? null : order.getConfirmationFromPartner().text())")
    DeliveryOrderResponseDto toDeliveryOrderResponseDto(DeliveryOrder order);

    void updateDeliveryOrderFromDto(UpdateDeliveryOrderRequestDto dto,
                                    @MappingTarget DeliveryOrder order);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "saleOrderId", source = "saleOrder.id")
    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    @Mapping(target = "deliveryType", expression = "java(order.getDeliveryType().text())")
    @Mapping(target = "confirmationFromFactory", expression = "java(order.getConfirmationFromFactory() == null ? null : order.getConfirmationFromFactory().text())")
    @Mapping(target = "confirmationFromPartner", expression = "java(order.getConfirmationFromPartner() == null ? null : order.getConfirmationFromPartner().text())")
    GetAllDeliveryOrderResponseDto toGetAllDeliveryOrderResponseDto(DeliveryOrder order);

    List<DeliveryOrderResponseDto> toDeliveryOrderResponseDtoList(List<DeliveryOrder> orders);

    // --- Elasticsearch Document related mappings ---
    GetAllDeliveryOrderResponseDto toResponseDtoFromDocument(DeliveryOrderDocument deliveryOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    DeliveryOrderDocument toDocument(DeliveryOrder entity);

    DeliveryOrder fromDocument(DeliveryOrderDocument doc);
}
