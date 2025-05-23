package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import com.structura.steel.commons.dto.core.request.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.PurchaseOrderDetailResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PurchaseOrderDetailMapper {

    PurchaseOrderDetail toPurchaseOrderDetail(PurchaseOrderDetailRequestDto dto);

    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    PurchaseOrderDetailResponseDto toPurchaseOrderDetailResponseDto(PurchaseOrderDetail detail);

    void updatePurchaseOrderDetailFromDto(PurchaseOrderDetailRequestDto dto,
                                          @MappingTarget PurchaseOrderDetail detail);

    List<PurchaseOrderDetailResponseDto> toPurchaseOrderDetailResponseDtoList(List<PurchaseOrderDetail> details);
}
