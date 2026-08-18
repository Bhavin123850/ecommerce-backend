package com.example.ecommerce.service;

import com.example.ecommerce.dto.review.ReviewRequest;
import com.example.ecommerce.dto.review.ReviewResponse;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewResponse createReview(
            UUID userId,
            UUID productId,
            ReviewRequest request) {

        if (reviewRepository
                .existsByUserIdAndProductId(
                        userId,
                        productId
                )) {

            throw new IllegalStateException(
                    "User already reviewed this product"
            );
        }

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        return mapToResponse(
                reviewRepository.save(review)
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getProductReviews(
            UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        return reviewRepository
                .findByProductId(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReviewResponse updateReview(
            UUID userId,
            UUID productId,
            UUID reviewId,
            ReviewRequest request) {

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"
                                ));

        if (!review.getUser()
                .getId()
                .equals(userId)) {

            throw new SecurityException(
                    "You cannot update this review"
            );
        }

        if (!review.getProduct()
                .getId()
                .equals(productId)) {

            throw new IllegalArgumentException(
                    "Review does not belong to this product"
            );
        }

        review.setRating(request.rating());
        review.setComment(request.comment());

        return mapToResponse(review);
    }

    public void deleteReview(
            UUID userId,
            UUID productId,
            UUID reviewId) {

        Review review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"
                                ));

        if (!review.getUser()
                .getId()
                .equals(userId)) {

            throw new SecurityException(
                    "You cannot delete this review"
            );
        }

        reviewRepository.delete(review);
    }

    private ReviewResponse mapToResponse(
            Review review) {

        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}