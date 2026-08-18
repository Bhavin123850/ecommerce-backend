package com.example.ecommerce.controller;

import com.example.ecommerce.dto.cart.AddCartItemRequest;
import com.example.ecommerce.dto.cart.CartResponse;
import com.example.ecommerce.dto.cart.UpdateCartItemRequest;
import com.example.ecommerce.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestParam UUID userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId)
        );
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestParam UUID userId,
            @Valid @RequestBody AddCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.addItem(userId, request)
        );
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(
            @RequestParam UUID userId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateItem(
                        userId,
                        itemId,
                        request
                )
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestParam UUID userId,
            @PathVariable UUID itemId) {

        return ResponseEntity.ok(
                cartService.removeItem(userId, itemId)
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestParam UUID userId) {

        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}