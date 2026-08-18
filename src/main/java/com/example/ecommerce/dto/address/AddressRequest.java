package com.example.ecommerce.dto.address;

import com.example.ecommerce.entity.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(

//        @NotBlank(message = "Full name is required")
//        @Size(max = 255)
//        String fullName,
//
//        @NotBlank(message = "Phone is required")
//        @Size(max = 20)
//        String phone,

        @NotBlank(message = "Address line 1 is required")
        @Size(max = 255)
        String addressLine1,

        @Size(max = 255)
        String addressLine2,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 100)
        String state,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20)
        String postalCode,

        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        @NotNull(message = "Address type is required")
        String addressType,

        boolean defaultAddress
) {

}