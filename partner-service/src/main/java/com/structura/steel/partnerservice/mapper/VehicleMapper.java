package com.structura.steel.partnerservice.mapper;

import com.structura.steel.partnerservice.dto.request.VehicleRequestDto;
import com.structura.steel.partnerservice.dto.response.VehicleResponseDto;
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

}
