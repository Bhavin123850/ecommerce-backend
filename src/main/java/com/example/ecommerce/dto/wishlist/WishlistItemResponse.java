package com.example.ecommerce.dto.wishlist;

import java.math.BigDecimal;
import java.util.UUID;

public record WishlistItemResponse(

        UUID id,

        UUID productId,

        String productName,

        BigDecimal price
) {
}