package com.example.ecommerce.dto.user;

import com.example.ecommerce.entity.UserRole;
import com.example.ecommerce.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String name,

        String email,

        String phone,

        UserRole role,

        UserStatus status,

        boolean emailVerified,

        Instant createdAt
) {
}