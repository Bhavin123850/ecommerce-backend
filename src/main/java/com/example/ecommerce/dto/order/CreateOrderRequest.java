package com.example.ecommerce.dto.order;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(

        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Address ID is required")
        UUID addressId
) {
}