package com.structura.steel.partnerservice.mapper;

import com.structura.steel.commons.dto.partner.request.WarehouseRequestDto;
import com.structura.steel.commons.dto.partner.response.WarehouseResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.WarehouseDocument;
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

    // --- Elasticsearch Document related mappings ---
    WarehouseResponseDto toResponseDtoFromDocument(WarehouseDocument warehouseDocument);

    @Mapping(target = "suggestion", ignore = true)
    @Mapping(target = "partnerId", source = "partner.id")
    WarehouseDocument toDocument(Warehouse entity);

    Warehouse fromDocument(WarehouseDocument doc);
}
