package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.request.DebtPaymentRequestDto;
import com.structura.steel.commons.dto.core.response.DebtPaymentResponseDto;
import com.structura.steel.coreservice.service.DebtPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class DebtPaymentController {

    private final DebtPaymentService debtPaymentService;

    @PostMapping
    public ResponseEntity<DebtPaymentResponseDto> recordPayment(
            @RequestBody DebtPaymentRequestDto dto) {
        return ResponseEntity.ok(debtPaymentService.recordPayment(dto));
    }
}