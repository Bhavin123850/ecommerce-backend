package com.example.ecommerce.dto.payment;

import com.example.ecommerce.entity.PaymentMethod;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentRequest(

        @NotNull(message = "Order ID is required")
        UUID orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod
) {
}