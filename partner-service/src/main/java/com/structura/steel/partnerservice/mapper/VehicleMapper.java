package com.structura.steel.partnerservice.mapper;

import com.structura.steel.dto.request.VehicleRequestDto;
import com.structura.steel.dto.response.VehicleResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import com.structura.steel.partnerservice.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface VehicleMapper {

    @Mapping(target = "partner.id", source = "partnerId")
    Vehicle toVehicle(VehicleRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    VehicleResponseDto toVehicleResponseDto(Vehicle entity);

    void updateVehicleFromDto(VehicleRequestDto dto, @MappingTarget Vehicle entity);

    List<VehicleResponseDto> toVehicleResponseDtoList(List<Vehicle> vehicles);

    // --- Elasticsearch Document related mappings ---
    VehicleResponseDto toResponseDtoFromDocument(VehicleDocument vehicleDocument);

    @Mapping(target = "suggestion", ignore = true)
    @Mapping(target = "partnerId", source = "partner.id")
    VehicleDocument toDocument(Vehicle entity);

    Vehicle fromDocument(VehicleDocument doc);
}
