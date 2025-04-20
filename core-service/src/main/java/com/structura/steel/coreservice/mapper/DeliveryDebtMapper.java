package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.DeliveryDebt;
import com.structura.steel.dto.request.DeliveryDebtRequestDto;
import com.structura.steel.dto.response.DeliveryDebtResponseDto;
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
