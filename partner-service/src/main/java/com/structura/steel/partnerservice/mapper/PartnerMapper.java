package com.structura.steel.partnerservice.mapper;

import com.structura.steel.partnerservice.dto.request.*;
import com.structura.steel.partnerservice.dto.response.*;
import com.structura.steel.partnerservice.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {PartnerProjectMapper.class, VehicleMapper.class, WarehouseMapper.class})
public interface PartnerMapper {

    Partner toPartner(PartnerRequestDto dto);
    PartnerResponseDto toPartnerResponseDto(Partner entity);
    void updatePartnerFromDto(PartnerRequestDto dto, @MappingTarget Partner entity);

    List<PartnerResponseDto> toPartnerResponseDtoList(List<Partner> partners);
}
