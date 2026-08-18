package com.example.ecommerce.dto.product;

import java.time.Instant;
import java.util.UUID;

public record ProductImageResponse(

        UUID id,

        String imageUrl,

        Instant createdAt
) {
}