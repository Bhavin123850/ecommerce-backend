package com.example.ecommerce.service;

import com.example.ecommerce.dto.order.CreateOrderRequest;
import com.example.ecommerce.dto.order.OrderResponse;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.OrderCancellationException;
import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    public OrderResponse createOrder(
            CreateOrderRequest request) throws RuntimeException{


        Cart cart =
                cartRepository.findByUserId(
                        request.userId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        ));

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException(
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
        if(address == null)
        {
            throw new ResourceNotFoundException(
                    "Address not found"
            );
        }

        Order order = Order.builder()
                .user(cart.getUser())
                .shippingAddress(address)
                .orderDate(Instant.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new InsufficientStockException(
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


        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) throws RuntimeException{

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

    public OrderResponse cancelOrder(UUID id) throws RuntimeException{

        Order order = getOrderEntity(id);

        if ((order.getStatus() == OrderStatus.DELIVERED) || (order.getStatus() == OrderStatus.SHIPPED)) {
            throw new OrderCancellationException(
                    "Delivered or Shipped order cannot be cancelled"
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderCancellationException(
                    "Order already cancelled"
            );
        }
        for(OrderItem orderItem : order.getItems())
        {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
        }
        order.setStatus(OrderStatus.CANCELLED);
        User user = order.getUser();

        Cart cart = cartRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        ));

        cart.getItems().clear();
        return mapToResponse(order);
    }

    public OrderResponse updateOrderStatus(
            UUID id,
            String status) throws RuntimeException{

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
        if (status == "SHIPPED") {

            User user = order.getUser();

            Cart cart = cartRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cart not found"
                            ));

            cart.getItems().clear();
        }
        return mapToResponse(order);
    }

    private Order getOrderEntity(UUID id) throws RuntimeException{

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
                order.getTotalAmount(),
                order.getOrderDate()
        );
    }
}
