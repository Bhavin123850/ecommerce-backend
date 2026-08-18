package com.example.ecommerce.service;

import com.example.ecommerce.dto.payment.PaymentRequest;
import com.example.ecommerce.dto.payment.PaymentResponse;

import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.ResourceNotFoundException;

import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentResponse createPayment(
            PaymentRequest request) {

        Order order =
                orderRepository.findById(
                        request.orderId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .paymentMethod(
                        request.paymentMethod()
                )
                .status(PaymentStatus.SUCCESS)
                .transactionId(
                        UUID.randomUUID().toString()
                )
                .paidAt(Instant.now())
                .build();

        order.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        Payment saved =
                paymentRepository.save(payment);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                ));

        return mapToResponse(payment);
    }

    public PaymentResponse refundPayment(UUID id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                ));

        if (payment.getStatus()
                != PaymentStatus.SUCCESS) {

            throw new IllegalStateException(
                    "Only successful payments can be refunded"
            );
        }

        payment.setStatus(
                PaymentStatus.REFUNDED
        );

        payment.getOrder().setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(
            Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getTransactionId(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getPaidAt()
        );
    }
}