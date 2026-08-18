package com.example.ecommerce.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ReviewRequest(

        @Min(value = 1,
                message = "Rating must be at least 1")
        @Max(value = 5,
                message = "Rating cannot exceed 5")
        Integer rating,

        @Size(max = 2000,
                message = "Comment cannot exceed 2000 characters")
        String comment
) {
}