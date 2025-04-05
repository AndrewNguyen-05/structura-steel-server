package com.structura.steel.partnerservice.mapper;

import com.structura.steel.dto.request.WarehouseRequestDto;
import com.structura.steel.dto.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface WarehouseMapper {

    @Mapping(target = "partner.id", source = "partnerId")
    Warehouse toWarehouse(WarehouseRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    WarehouseResponseDto toWarehouseResponseDto(Warehouse entity);

    void updateWarehouseFromDto(WarehouseRequestDto dto, @MappingTarget Warehouse entity);

    List<WarehouseResponseDto> toWarehouseResponseDtoList(List<Warehouse> warehouses);
}
