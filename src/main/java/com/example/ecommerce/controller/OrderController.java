package com.example.ecommerce.controller;

import com.example.ecommerce.dto.order.CreateOrderRequest;
import com.example.ecommerce.dto.order.OrderResponse;

import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                orderService.getOrder(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @RequestParam UUID userId,
            Pageable pageable) {

        return ResponseEntity.ok(
                orderService.getUserOrders(
                        userId,
                        pageable
                )
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                orderService.cancelOrder(id)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        id,
                        status
                )
        );
    }
}