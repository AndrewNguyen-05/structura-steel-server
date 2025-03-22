package com.structura.steel.partnerservice.mapper;

import com.structura.steel.partnerservice.dto.request.*;
import com.structura.steel.partnerservice.dto.response.*;
import com.structura.steel.partnerservice.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface PartnerMapper {

    //Partner
    Partner toPartner(PartnerRequestDto dto);
    PartnerResponseDto toPartnerResponseDto(Partner entity);
    void updatePartnerFromDto(PartnerRequestDto dto, @MappingTarget Partner entity);

    //PartnerProject
    @Mapping(target = "partner.id", source = "partnerId")
    PartnerProject toPartnerProject(PartnerProjectRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    PartnerProjectResponseDto toPartnerProjectResponseDto(PartnerProject entity);

    void updatePartnerProjectFromDto(PartnerProjectRequestDto dto, @MappingTarget PartnerProject entity);

    //Vehicle
    @Mapping(target = "partner.id", source = "partnerId")
    Vehicle toVehicle(VehicleRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    VehicleResponseDto toVehicleResponseDto(Vehicle entity);

    void updateVehicleFromDto(VehicleRequestDto dto, @MappingTarget Vehicle entity);

    //Warehouse
    @Mapping(target = "partner.id", source = "partnerId")
    Warehouse toWarehouse(WarehouseRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    WarehouseResponseDto toWarehouseResponseDto(Warehouse entity);

    void updateWarehouseFromDto(WarehouseRequestDto dto, @MappingTarget Warehouse entity);

    List<PartnerResponseDto> toPartnerResponseDtoList(List<Partner> partners);
    List<PartnerProjectResponseDto> toPartnerProjectResponseDtoList(List<PartnerProject> projects);
    List<VehicleResponseDto> toVehicleResponseDtoList(List<Vehicle> vehicles);
    List<WarehouseResponseDto> toWarehouseResponseDtoList(List<Warehouse> warehouses);
}
