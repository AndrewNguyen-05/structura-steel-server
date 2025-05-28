package com.structura.steel.commons.dto.partner.request;

import com.structura.steel.commons.enumeration.DebtAccountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePartnerDebtRequestDto(
        @NotNull
        BigDecimal amount, // Số tiền thay đổi (có thể âm hoặc dương)

        @NotNull
        DebtAccountType accountType
) {}