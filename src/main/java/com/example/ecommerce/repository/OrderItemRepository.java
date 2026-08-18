package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, UUID> {
}