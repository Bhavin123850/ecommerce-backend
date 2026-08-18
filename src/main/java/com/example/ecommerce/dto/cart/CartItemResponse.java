package com.example.ecommerce.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(

        UUID id,

        UUID productId,

        String productName,

        BigDecimal price,

        Integer quantity
) {
}