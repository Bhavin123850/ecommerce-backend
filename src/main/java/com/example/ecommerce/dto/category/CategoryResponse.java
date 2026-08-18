package com.example.ecommerce.dto.category;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(

        UUID id,

        String name,

        String description,

        boolean active,

        Instant createdAt,

        Instant updatedAt
) {
}

