package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;

public interface DebtPaymentService {

    DebtPaymentResponseDto recordPayment(DebtPaymentRequestDto dto);
}