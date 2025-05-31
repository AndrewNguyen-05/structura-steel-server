package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.partner.response.PartnerProjectResponseDto;
import com.structura.steel.commons.dto.partner.response.PartnerResponseDto;
import com.structura.steel.commons.dto.partner.response.WarehouseResponseDto;
import com.structura.steel.commons.dto.product.response.ProductResponseDto;
import com.structura.steel.coreservice.entity.embedded.Partner;
import com.structura.steel.coreservice.entity.embedded.PartnerProject;
import com.structura.steel.coreservice.entity.embedded.Product;
import com.structura.steel.coreservice.entity.embedded.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PartnerMapper {

    @Mapping(target = "warehouses", source = "partnerDto.warehouses")
    Partner toSupplierSnapshot(PartnerResponseDto partnerDto);

    PartnerProject toProjectSnapshot(PartnerProjectResponseDto projectDto);

    Product toProductSnapshot(ProductResponseDto productDto);

    PartnerResponseDto toPartnerResponseDto(Partner partner);

    PartnerProjectResponseDto toProjectResponseDto(PartnerProject project);

    ProductResponseDto toProductResponseDto(Product product);

    WarehouseResponseDto toWarehouseResponseDto(Warehouse warehouseSnapshot);
}
