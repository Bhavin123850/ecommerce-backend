package com.example.ecommerce.dto.product;

import com.example.ecommerce.entity.Product;

import java.time.Instant;
import java.util.UUID;

public record ProductImageResponse(

        UUID id,
        UUID productId,
        String imageUrl,
        boolean primary,
        Instant createdAt
) {
}