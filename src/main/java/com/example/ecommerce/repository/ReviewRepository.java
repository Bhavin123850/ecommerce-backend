package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository
        extends JpaRepository<Review, UUID> {

    List<Review> findByProductId(UUID productId);

    boolean existsByUserIdAndProductId(
            UUID userId,
            UUID productId
    );
}