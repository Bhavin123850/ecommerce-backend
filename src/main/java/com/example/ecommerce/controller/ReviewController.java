package com.example.ecommerce.controller;

import com.example.ecommerce.dto.review.ReviewRequest;
import com.example.ecommerce.dto.review.ReviewResponse;
import com.example.ecommerce.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable UUID productId,
            @RequestParam UUID userId,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response =
                reviewService.createReview(
                        userId,
                        productId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                reviewService.getProductReviews(productId)
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable UUID productId,
            @PathVariable UUID reviewId,
            @RequestParam UUID userId,
            @Valid @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.updateReview(
                        userId,
                        productId,
                        reviewId,
                        request
                )
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID productId,
            @PathVariable UUID reviewId,
            @RequestParam UUID userId) {

        reviewService.deleteReview(
                userId,
                productId,
                reviewId
        );

        return ResponseEntity.noContent().build();
    }
}