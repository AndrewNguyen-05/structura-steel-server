package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseOrderDetailRequestDto;
import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseOrderDetailRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseOrderDetailResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PurchaseOrderDetailMapper {

    PurchaseOrderDetail toPurchaseOrderDetail(PurchaseOrderDetailRequestDto dto);

    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    PurchaseOrderDetailResponseDto toPurchaseOrderDetailResponseDto(PurchaseOrderDetail detail);

    void updatePurchaseOrderDetailFromDto(UpdatePurchaseOrderDetailRequestDto dto,
                                          @MappingTarget PurchaseOrderDetail detail);

    List<PurchaseOrderDetailResponseDto> toPurchaseOrderDetailResponseDtoList(List<PurchaseOrderDetail> details);


}
