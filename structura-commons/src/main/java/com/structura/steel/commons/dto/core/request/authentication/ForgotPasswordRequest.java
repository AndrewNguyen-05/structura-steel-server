package com.structura.steel.commons.dto.core.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

        @NotBlank(message = "Email can not blank")
        @Email(message = "Invalid email format")
        String email
) {}
