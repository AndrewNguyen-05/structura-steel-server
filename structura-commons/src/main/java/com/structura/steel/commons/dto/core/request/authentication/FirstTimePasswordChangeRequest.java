package com.structura.steel.commons.dto.core.request.authentication;

import com.structura.steel.commons.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record FirstTimePasswordChangeRequest(

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Temporary password cannot be blank")
        String temporaryPassword,

        @NotBlank(message = "New password cannot be blank")
        @ValidPassword
        String newPassword
) {}