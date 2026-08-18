package com.example.ecommerce.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 255)
        String name,

        @Size(max = 1000)
        String description,

        @NotBlank(message = "SKU is required")
        @Size(max = 100)
        String sku,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0",
                inclusive = false,
                message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2)
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0,
                message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @NotNull(message = "Category is required")
        UUID categoryId
) {
}