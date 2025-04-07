package com.structura.steel.dto.response;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName
) {}
