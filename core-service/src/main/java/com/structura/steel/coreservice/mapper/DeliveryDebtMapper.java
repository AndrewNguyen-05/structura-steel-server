package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.DeliveryDebt;
import com.structura.steel.commons.dto.core.request.delivery.DeliveryDebtRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryDebtResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface DeliveryDebtMapper {

    @Mapping(target = "deliveryOrder", ignore = true)
    DeliveryDebt toDeliveryDebt(DeliveryDebtRequestDto dto);

    @Mapping(source = "deliveryOrder.id", target = "deliveryOrderId")
    DeliveryDebtResponseDto toDeliveryDebtResponseDto(DeliveryDebt debt);

    @Mapping(target = "deliveryOrder", ignore = true)
    void updateDeliveryDebtFromDto(DeliveryDebtRequestDto dto, @MappingTarget DeliveryDebt debt);

    List<DeliveryDebtResponseDto> toDeliveryDebtResponseDtoList(List<DeliveryDebt> debts);
}
