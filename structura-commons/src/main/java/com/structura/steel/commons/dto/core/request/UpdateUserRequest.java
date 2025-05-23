package com.structura.steel.commons.dto.core.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "First name can not blank")
        String firstName,
        @NotBlank(message = "Last name can not blank")
        String lastName,
        @NotBlank(message = "Email can not blank")
        String email,
        String password,
        String realmRole
) {}

