package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.PurchaseDebt;
import com.structura.steel.dto.request.PurchaseDebtRequestDto;
import com.structura.steel.dto.response.PurchaseDebtResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PurchaseDebtMapper {

    @Mapping(target = "purchaseOrder", ignore = true)
    PurchaseDebt toPurchaseDebt(PurchaseDebtRequestDto dto);

    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    PurchaseDebtResponseDto toPurchaseDebtResponseDto(PurchaseDebt debt);

    @Mapping(target = "purchaseOrder", ignore = true)
    void updatePurchaseDebtFromDto(PurchaseDebtRequestDto dto, @MappingTarget PurchaseDebt debt);

    List<PurchaseDebtResponseDto> toPurchaseDebtResponseDtoList(List<PurchaseDebt> debts);
}
