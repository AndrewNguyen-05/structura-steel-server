package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderRequestDto;
import com.structura.steel.coreservice.elasticsearch.document.PurchaseOrderDocument;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.GetAllPurchaseOrderResponseDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {PurchaseOrderDetailMapper.class, PurchaseDebtMapper.class})
public interface PurchaseOrderMapper {

    PurchaseOrder toPurchaseOrder(PurchaseOrderRequestDto dto);

    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    PurchaseOrderResponseDto toPurchaseOrderResponseDto(PurchaseOrder order);

    void updatePurchaseOrderFromDto(UpdatePurchaseOrderRequestDto dto,
                                    @MappingTarget PurchaseOrder order);

    @Mapping(target = "supplierId", source = "order.supplier.id")
    @Mapping(target = "supplierName", source = "order.supplier.partnerName")
    @Mapping(target = "supplierCode", source = "order.supplier.partnerCode")
    @Mapping(target = "projectId", source = "order.project.id", conditionExpression = "java(order.getProject() != null)")
    @Mapping(target = "projectName", source = "order.project.projectName", conditionExpression = "java(order.getProject() != null)")
    @Mapping(target = "projectCode", source = "order.project.projectCode", conditionExpression = "java(order.getProject() != null)")
    @Mapping(target = "status", expression = "java(order.getStatus().text())")
    GetAllPurchaseOrderResponseDto toGetAllPurchaseOrderResponseDto(PurchaseOrder order);

    List<PurchaseOrderResponseDto> toPurchaseOrderResponseDtoList(List<PurchaseOrder> orders);

    // --- Elasticsearch Document related mappings ---
    GetAllPurchaseOrderResponseDto toResponseDtoFromDocument(PurchaseOrderDocument purchaseOrderDocument);

    @Mapping(target = "suggestion", ignore = true)
    PurchaseOrderDocument toDocument(PurchaseOrder entity);

    PurchaseOrder fromDocument(PurchaseOrderDocument doc);
}
