package com.example.ecommerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 255)
        String name,

        @Size(max = 1000)
        String description,

        boolean active
) {
}