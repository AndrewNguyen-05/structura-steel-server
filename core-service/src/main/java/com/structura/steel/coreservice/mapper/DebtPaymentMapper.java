package com.structura.steel.coreservice.mapper;

import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;
import com.structura.steel.coreservice.entity.DebtPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DebtPaymentMapper {

    DebtPayment toDebtPayment(DebtPaymentRequestDto dto);

    DebtPaymentResponseDto toDebtPaymentResponseDto(DebtPayment entity);
}