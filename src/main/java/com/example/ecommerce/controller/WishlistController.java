package com.example.ecommerce.controller;

import com.example.ecommerce.dto.wishlist.WishlistResponse;
import com.example.ecommerce.service.WishlistService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist(
            @RequestParam UUID userId) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(userId)
        );
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<WishlistResponse> addProduct(
            @RequestParam UUID userId,
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                wishlistService.addProduct(
                        userId,
                        productId
                )
        );
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<WishlistResponse> removeProduct(
            @RequestParam UUID userId,
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                wishlistService.removeProduct(
                        userId,
                        productId
                )
        );
    }
}