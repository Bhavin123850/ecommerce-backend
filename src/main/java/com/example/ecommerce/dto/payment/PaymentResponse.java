package com.example.ecommerce.dto.payment;

import com.example.ecommerce.entity.PaymentMethod;
import com.example.ecommerce.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(

        UUID id,

        UUID orderId,

        String transactionId,

        PaymentMethod paymentMethod,

        PaymentStatus status,

        BigDecimal amount,

        Instant paidAt
) {
}