package com.structura.steel.partnerservice.mapper;

import com.structura.steel.dto.request.PartnerRequestDto;
import com.structura.steel.dto.response.GetAllPartnerResponseDto;
import com.structura.steel.dto.response.PartnerResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.entity.*;
import org.mapstruct.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Mapper(uses = {PartnerProjectMapper.class, VehicleMapper.class, WarehouseMapper.class})
public interface PartnerMapper {

    Partner toPartner(PartnerRequestDto dto);

    PartnerResponseDto toPartnerResponseDto(Partner entity);

    void updatePartnerFromDto(PartnerRequestDto dto, @MappingTarget Partner entity);

    List<PartnerResponseDto> toPartnerResponseDtoList(List<Partner> partners);

    // --- Elasticsearch Document related mappings ---
    GetAllPartnerResponseDto toResponseDtoFromDocument(PartnerDocument partnerDocument);

    @Mapping(target = "suggestion", ignore = true)
    PartnerDocument toDocument(Partner entity);

    Partner fromDocument(PartnerDocument doc);
}
