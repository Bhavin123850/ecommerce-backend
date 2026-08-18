package com.example.ecommerce.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCartItemRequest(

        @NotNull(message = "Product ID is required")
        UUID productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1,
                message = "Quantity must be at least 1")
        Integer quantity
) {
}