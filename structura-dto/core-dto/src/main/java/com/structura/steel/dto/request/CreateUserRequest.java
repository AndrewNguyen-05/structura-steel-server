package com.structura.steel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Username can not blank")
        @Size(min = 3, max = 50, message = "Username's length must be from 3 to 50 characters")
        String username,
        @NotBlank(message = "Password can not blank")
        @Size(min = 3, max = 100, message = "Password's length must be from 8 to 100 characters")
        String password,
        @NotBlank(message = "Email can not blank")
        @Email
        String email,
        @NotBlank(message = "First name can not blank")
        String firstName,
        @NotBlank(message = "Last name can not blank")
        String lastName,
        @NotBlank(message = "Role can not blank")
        String realmRole
) {}

