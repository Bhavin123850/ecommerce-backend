package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository
        extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCartIdAndProductId(
            UUID cartId,
            UUID productId
    );

    Optional<CartItem> findByIdAndCartId(
            UUID id,
            UUID cartId
    );
}