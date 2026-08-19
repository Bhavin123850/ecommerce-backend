package com.example.ecommerce.dto.cart;

import java.util.List;
import java.util.UUID;

public record CartResponse(

        UUID id,

        UUID userID,

        List<CartItemResponse> items
) {
}