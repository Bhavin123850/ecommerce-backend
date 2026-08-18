package com.example.ecommerce.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(

        UUID id,

        String name,

        String description,

        String sku,

        BigDecimal price,

        Integer stockQuantity,

        boolean active,

        UUID categoryId
) {
}