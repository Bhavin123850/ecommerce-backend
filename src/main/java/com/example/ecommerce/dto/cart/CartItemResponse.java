package com.example.ecommerce.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(

        UUID id,

        UUID cartId,

        UUID productId,

        String productName,

        BigDecimal totalAmount,

        Integer quantity
) {
}