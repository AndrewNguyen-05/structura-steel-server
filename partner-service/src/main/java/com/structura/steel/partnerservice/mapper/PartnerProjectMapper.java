package com.structura.steel.partnerservice.mapper;

import com.structura.steel.dto.request.PartnerProjectRequestDto;
import com.structura.steel.dto.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface PartnerProjectMapper {

    @Mapping(target = "partner.id", source = "partnerId")
    PartnerProject toPartnerProject(PartnerProjectRequestDto dto);

    @Mapping(target = "partnerId", source = "partner.id")
    PartnerProjectResponseDto toPartnerProjectResponseDto(PartnerProject entity);

    void updatePartnerProjectFromDto(PartnerProjectRequestDto dto, @MappingTarget PartnerProject entity);

    List<PartnerProjectResponseDto> toPartnerProjectResponseDtoList(List<PartnerProject> projects);

    // --- Elasticsearch Document related mappings ---
    PartnerProjectResponseDto toResponseDtoFromDocument(PartnerProjectDocument partnerProjectDocument);

    @Mapping(target = "suggestion", ignore = true)
    @Mapping(target = "partnerId", source = "partner.id")
    PartnerProjectDocument toDocument(PartnerProject entity);

    PartnerProject fromDocument(PartnerProjectDocument doc);
}
