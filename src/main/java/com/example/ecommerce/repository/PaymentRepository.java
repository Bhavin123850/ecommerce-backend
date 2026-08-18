package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {
}