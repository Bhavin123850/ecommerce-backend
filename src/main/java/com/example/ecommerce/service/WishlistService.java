package com.example.ecommerce.service;

import com.example.ecommerce.dto.wishlist.WishlistItemResponse;
import com.example.ecommerce.dto.wishlist.WishlistResponse;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public WishlistResponse getWishlist(
            UUID userId) {

        Wishlist wishlist =
                getOrCreateWishlist(userId);

        return mapToResponse(wishlist);
    }

    public WishlistResponse addProduct(
            UUID userId,
            UUID productId) {

        Wishlist wishlist =
                getOrCreateWishlist(userId);

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        if (itemRepository
                .findByWishlistIdAndProductId(
                        wishlist.getId(),
                        productId
                )
                .isPresent()) {

            return mapToResponse(wishlist);
        }

        WishlistItem item =
                WishlistItem.builder()
                        .wishlist(wishlist)
                        .product(product)
                        .build();

        itemRepository.save(item);

        return mapToResponse(wishlist);
    }

    public WishlistResponse removeProduct(
            UUID userId,
            UUID productId) {

        Wishlist wishlist =
                getOrCreateWishlist(userId);

        WishlistItem item =
                itemRepository
                        .findByWishlistIdAndProductId(
                                wishlist.getId(),
                                productId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not in wishlist"
                                ));

        itemRepository.delete(item);

        return mapToResponse(wishlist);
    }

    private Wishlist getOrCreateWishlist(
            UUID userId) {

        return wishlistRepository
                .findByUserId(userId)
                .orElseGet(() -> {

                    User user =
                            userRepository.findById(userId)
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "User not found"
                                            ));

                    Wishlist wishlist =
                            Wishlist.builder()
                                    .user(user)
                                    .build();

                    return wishlistRepository.save(
                            wishlist
                    );
                });
    }

    private WishlistResponse mapToResponse(
            Wishlist wishlist) {

        var items = wishlist.getItems()
                .stream()
                .map(item ->
                        new WishlistItemResponse(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getPrice()
                        )
                )
                .toList();

        return new WishlistResponse(
                wishlist.getId(),
                items
        );
    }
}