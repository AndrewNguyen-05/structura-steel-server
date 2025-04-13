package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.SaleDebt;
import com.structura.steel.dto.request.SaleDebtRequestDto;
import com.structura.steel.dto.response.SaleDebtResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface SaleDebtMapper {

    @Mapping(target = "saleOrder", ignore = true) // Xử lý ánh xạ saleOrder trong service hoặc custom mapper nếu cần
    SaleDebt toSaleDebt(SaleDebtRequestDto dto);

    @Mapping(source = "saleOrder.id", target = "saleOrderId")
    SaleDebtResponseDto toSaleDebtResponseDto(SaleDebt saleDebt);

    @Mapping(target = "saleOrder", ignore = true)
    void updateSaleDebtFromDto(SaleDebtRequestDto dto, @MappingTarget SaleDebt saleDebt);

    List<SaleDebtResponseDto> toSaleDebtResponseDtoList(List<SaleDebt> saleDebts);
}
