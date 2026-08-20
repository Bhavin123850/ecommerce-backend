package com.example.ecommerce.dto.order;

import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(

        UUID id,

        UUID userId,

        OrderStatus status,

        BigDecimal totalAmount,

        Instant orderDate
) {
}