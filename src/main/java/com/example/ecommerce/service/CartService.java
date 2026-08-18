package com.example.ecommerce.service;

import com.example.ecommerce.dto.cart.AddCartItemRequest;
import com.example.ecommerce.dto.cart.CartItemResponse;
import com.example.ecommerce.dto.cart.CartResponse;
import com.example.ecommerce.dto.cart.UpdateCartItemRequest;

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
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CartResponse getCart(UUID userId) {

        Cart cart = getOrCreateCart(userId);

        return mapToResponse(cart);
    }

    public CartResponse addItem(
            UUID userId,
            AddCartItemRequest request) {

        Cart cart = getOrCreateCart(userId);

        Product product =
                productRepository.findById(
                        request.productId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));

        if (!product.isActive()) {
            throw new IllegalArgumentException(
                    "Product is not active"
            );
        }

        if (product.getStockQuantity()
                < request.quantity()) {

            throw new IllegalArgumentException(
                    "Insufficient stock"
            );
        }

        CartItem item =
                cartItemRepository
                        .findByCartIdAndProductId(
                                cart.getId(),
                                product.getId()
                        )
                        .orElse(null);

        if (item == null) {

            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();

        } else {

            int newQuantity =
                    item.getQuantity()
                            + request.quantity();

            if (newQuantity >
                    product.getStockQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock"
                );
            }

            item.setQuantity(newQuantity);
        }

        cartItemRepository.save(item);

        return mapToResponse(cart);
    }

    public CartResponse updateItem(
            UUID userId,
            UUID itemId,
            UpdateCartItemRequest request) {

        Cart cart = getOrCreateCart(userId);

        CartItem item =
                cartItemRepository
                        .findByIdAndCartId(
                                itemId,
                                cart.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"
                                ));

        Product product = item.getProduct();

        if (request.quantity() >
                product.getStockQuantity()) {

            throw new IllegalArgumentException(
                    "Insufficient stock"
            );
        }

        item.setQuantity(request.quantity());

        return mapToResponse(cart);
    }

    public CartResponse removeItem(
            UUID userId,
            UUID itemId) {

        Cart cart = getOrCreateCart(userId);

        CartItem item =
                cartItemRepository
                        .findByIdAndCartId(
                                itemId,
                                cart.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"
                                ));

        cartItemRepository.delete(item);

        return mapToResponse(cart);
    }

    public void clearCart(UUID userId) {

        Cart cart = getOrCreateCart(userId);

        cart.getItems().clear();
    }

    private Cart getOrCreateCart(UUID userId) {

        return cartRepository
                .findByUserId(userId)
                .orElseGet(() -> {

                    User user =
                            userRepository.findById(userId)
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "User not found"
                                            ));

                    Cart cart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(cart);
                });
    }

    private CartResponse mapToResponse(Cart cart) {

        var items = cart.getItems()
                .stream()
                .map(item ->
                        new CartItemResponse(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity()
                        )
                )
                .toList();

        return new CartResponse(
                cart.getId(),
                items
        );
    }
}