package com.example.ecommerce.controller;

import com.example.ecommerce.dto.address.AddressRequest;
import com.example.ecommerce.dto.address.AddressResponse;
import com.example.ecommerce.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<?> createAddress(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressRequest request) {
        try {
            AddressResponse response =
                    addressService.createAddress(userId, request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAddresses(
            @PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(
                    addressService.getUserAddresses(userId)
            );
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        }
    }
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(
            @PathVariable UUID userId,
            @PathVariable UUID addressId) {
        try {
            return ResponseEntity.ok(
                    addressService.getAddress(userId, addressId)
            );
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable UUID userId,
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request) {
        try {
            return ResponseEntity.ok(
                    addressService.updateAddress(
                            userId,
                            addressId,
                            request
                    )
            );
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable UUID userId,
            @PathVariable UUID addressId) {
        try {
            addressService.deleteAddress(userId, addressId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<?> makeDefault(
            @PathVariable UUID userId,
            @PathVariable UUID addressId) {
        try {
            addressService.makeDefault(userId, addressId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}