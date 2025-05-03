package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.PurchaseOrder;
import com.structura.steel.dto.request.PurchaseOrderRequestDto;
import com.structura.steel.dto.response.GetAllPurchaseOrderResponseDto;
import com.structura.steel.dto.response.PurchaseOrderResponseDto;
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
}
