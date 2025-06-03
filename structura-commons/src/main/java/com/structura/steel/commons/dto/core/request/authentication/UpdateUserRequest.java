package com.structura.steel.commons.dto.core.request.authentication;

import com.structura.steel.commons.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

        @NotBlank(message = "First name can not blank")
        String firstName,

        @NotBlank(message = "Last name can not blank")
        String lastName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email can not blank")
        String email,

        @ValidPassword
        String password,
        String realmRole
) {}

