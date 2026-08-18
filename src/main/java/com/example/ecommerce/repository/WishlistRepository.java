package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository
        extends JpaRepository<Wishlist, UUID> {

    Optional<Wishlist> findByUserId(UUID userId);
}