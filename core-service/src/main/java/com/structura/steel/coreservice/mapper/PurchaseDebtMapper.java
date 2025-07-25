package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.purchase.UpdatePurchaseDebtRequestDto;
import com.structura.steel.coreservice.entity.PurchaseDebt;
import com.structura.steel.commons.dto.core.request.purchase.PurchaseDebtRequestDto;
import com.structura.steel.commons.dto.core.response.purchase.PurchaseDebtResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PurchaseDebtMapper {

    PurchaseDebt toPurchaseDebt(PurchaseDebtRequestDto dto);

    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    PurchaseDebtResponseDto toPurchaseDebtResponseDto(PurchaseDebt debt);

    @Mapping(target = "purchaseOrder", ignore = true)
    void updatePurchaseDebtFromDto(UpdatePurchaseDebtRequestDto dto, @MappingTarget PurchaseDebt debt);

    List<PurchaseDebtResponseDto> toPurchaseDebtResponseDtoList(List<PurchaseDebt> debts);
}
