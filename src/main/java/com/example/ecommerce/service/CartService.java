package com.example.ecommerce.service;

import com.example.ecommerce.dto.cart.AddCartItemRequest;
import com.example.ecommerce.dto.cart.CartItemResponse;
import com.example.ecommerce.dto.cart.CartResponse;
import com.example.ecommerce.dto.cart.UpdateCartItemRequest;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public CartResponse getCart(UUID userId) throws RuntimeException{

        Cart cart = getOrCreateCart(userId);
        return mapToResponse(cart);
    }

    public CartItemResponse addItem(
            UUID userId,
            AddCartItemRequest request) throws Exception{

        Cart cart = getOrCreateCart(userId);

        Product product =
                productRepository.findById(
                        request.productId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));

        if (!product.isActive()) {
            throw new ResourceNotFoundException(
                    "Product is not active"
            );
        }

        if (product.getStockQuantity()
                < request.quantity()) {

            throw new InsufficientStockException(
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

                throw new InsufficientStockException(
                        "Insufficient stock"
                );
            }

            item.setQuantity(newQuantity);
        }

        cartItemRepository.save(item);

        return mapToCartItemResponse(item);
    }

    public CartItemResponse updateItem(
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

            throw new InsufficientStockException(
                    "Insufficient stock"
            );
        }

        item.setQuantity(request.quantity());

        return mapToCartItemResponse(item);
    }

    public CartResponse removeItem(
            UUID userId,
            UUID itemId) throws RuntimeException{

        Cart cart;
        try {
            cart = getOrCreateCart(userId);
        }
        catch(Exception e)
        {
            throw e;
        }

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

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return mapToResponse(cart);
    }

    public void clearCart(UUID userId) throws RuntimeException{

        Cart cart = getOrCreateCart(userId);

        cart.getItems().clear();
    }

    private Cart getOrCreateCart(UUID userId) throws RuntimeException {

        return cartRepository
                .findByUserId(userId)
//                .get()
                .orElseGet(() -> {

                    User user =
                            userRepository.findById(userId)
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "User not found"
                                            ));

                    Cart cart1 = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(cart1);
                });
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem)
    {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getCart().getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())),
                cartItem.getQuantity());
    }

    private CartResponse mapToResponse(Cart cart) {

        var items = cart.getItems()
                .stream()
                .map(item ->
                        new CartItemResponse(
                                item.getId(),
                                item.getCart().getId(),
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity()
                        )
                )
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items
        );
    }
}