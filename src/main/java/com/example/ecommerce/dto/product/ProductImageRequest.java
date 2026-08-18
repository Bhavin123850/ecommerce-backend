package com.example.ecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProductImageRequest(

        @NotBlank(message = "Image URL is required")
        @Size(max = 1000)
        String imageUrl,
        @NotNull(message = "Product is required")
        UUID productId,
        @NotNull(message = "Boolean value of Primary is required")
        boolean primary
) {
}