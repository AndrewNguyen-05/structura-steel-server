package com.structura.steel.commons.dto.core.response;

import java.time.Instant;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String realmRole,
        Short version,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}
