package com.example.ecommerce.dto.review;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(

        UUID id,

        UUID userId,

        UUID productId,

        Integer rating,

        String comment,

        Instant createdAt
) {
}