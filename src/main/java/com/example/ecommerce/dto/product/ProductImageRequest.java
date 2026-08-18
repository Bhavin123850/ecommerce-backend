package com.example.ecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductImageRequest(

        @NotBlank(message = "Image URL is required")
        @Size(max = 1000)
        String imageUrl
) {
}