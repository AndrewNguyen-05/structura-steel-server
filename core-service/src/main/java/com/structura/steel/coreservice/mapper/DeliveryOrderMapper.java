package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.DeliveryOrder;
import com.structura.steel.dto.request.DeliveryOrderRequestDto;
import com.structura.steel.dto.response.DeliveryOrderResponseDto;
import com.structura.steel.dto.response.GetAllDeliveryOrderResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {DeliveryDebtMapper.class})
public interface DeliveryOrderMapper {

    DeliveryOrder toDeliveryOrder(DeliveryOrderRequestDto dto);

    DeliveryOrderResponseDto toDeliveryOrderResponseDto(DeliveryOrder order);

    void updateDeliveryOrderFromDto(DeliveryOrderRequestDto dto,
                                    @MappingTarget DeliveryOrder order);

    GetAllDeliveryOrderResponseDto toGetAllDeliveryOrderResponseDto(DeliveryOrder order);

    List<DeliveryOrderResponseDto> toDeliveryOrderResponseDtoList(List<DeliveryOrder> orders);
}
