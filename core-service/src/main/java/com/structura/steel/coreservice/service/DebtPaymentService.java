package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;
import com.structura.steel.commons.enumeration.DebtType;

import java.util.List;

public interface DebtPaymentService {

    DebtPaymentResponseDto recordPayment(DebtPaymentRequestDto dto);

    List<DebtPaymentResponseDto> getPaymentHistoryForOrder(Long orderId, DebtType debtType);
}