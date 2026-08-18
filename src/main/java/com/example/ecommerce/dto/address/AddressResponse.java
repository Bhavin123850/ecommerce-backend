package com.example.ecommerce.dto.address;

import com.example.ecommerce.entity.Address;

import java.util.UUID;

public record AddressResponse(

        UUID id,

        String addressLine1,

        String addressLine2,

        String city,

        String state,

        String postalCode,

        String country,

        String addressType,

        boolean defaultAddress
) {
}