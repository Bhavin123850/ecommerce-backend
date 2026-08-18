package com.example.ecommerce.dto.wishlist;

import java.util.List;
import java.util.UUID;

public record WishlistResponse(

        UUID id,

        List<WishlistItemResponse> items
) {
}