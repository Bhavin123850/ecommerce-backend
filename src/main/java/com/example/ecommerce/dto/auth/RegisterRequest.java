package com.example.ecommerce.dto.auth;

import com.example.ecommerce.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name cannot exceed 255 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100,
                message = "Password must be between 8 and 100 characters")
        String password,

        @Size(max = 20, message = "Phone cannot exceed 20 characters")
        String phone,

        UserRole role
) {
}