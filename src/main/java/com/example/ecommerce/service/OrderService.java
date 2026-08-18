package com.example.ecommerce.service;

import com.example.ecommerce.dto.order.CreateOrderRequest;
import com.example.ecommerce.dto.order.OrderResponse;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;

    public OrderResponse createOrder(
            CreateOrderRequest request) {

        Cart cart =
                cartRepository.findByUserId(
                        request.userId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        ));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot create order from empty cart"
            );
        }

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                request.addressId(),
                                request.userId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        Order order = Order.builder()
                .user(cart.getUser())
                .shippingAddress(address)
                .orderDate(Instant.now())
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new IllegalStateException(
                        "Insufficient stock for "
                                + product.getName()
                );
            }

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(
                                    cartItem.getQuantity()
                            )
                            .price(product.getPrice())
                            .build();

            order.getItems().add(orderItem);

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            );

            total = total.add(itemTotal);

            product.setStockQuantity(
                    product.getStockQuantity()
                            - cartItem.getQuantity()
            );
        }

        order.setTotalAmount(total);

        Order savedOrder =
                orderRepository.save(order);

        cart.getItems().clear();

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {

        return mapToResponse(
                getOrderEntity(id)
        );
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(
            UUID userId,
            Pageable pageable) {

        return orderRepository
                .findByUserId(userId, pageable)
                .map(this::mapToResponse);
    }

    public OrderResponse cancelOrder(UUID id) {

        Order order = getOrderEntity(id);

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Delivered order cannot be cancelled"
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Order already cancelled"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        return mapToResponse(order);
    }

    public OrderResponse updateOrderStatus(
            UUID id,
            String status) {

        Order order = getOrderEntity(id);

        try {
            order.setStatus(
                    OrderStatus.valueOf(
                            status.toUpperCase()
                    )
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid order status"
            );
        }

        return mapToResponse(order);
    }

    private Order getOrderEntity(UUID id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));
    }

    private OrderResponse mapToResponse(
            Order order) {

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getTotalAmount(),
                order.getOrderDate()
        );
    }
}