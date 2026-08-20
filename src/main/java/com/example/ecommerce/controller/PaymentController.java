//package com.example.ecommerce.controller;
//
//import com.example.ecommerce.dto.payment.PaymentRequest;
//import com.example.ecommerce.dto.payment.PaymentResponse;
//import com.example.ecommerce.service.PaymentService;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    @PostMapping
//    public ResponseEntity<PaymentResponse> createPayment(
//            @Valid @RequestBody PaymentRequest request) {
//
//        PaymentResponse response =
//                paymentService.createPayment(request);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PaymentResponse> getPayment(
//            @PathVariable UUID id) {
//
//        return ResponseEntity.ok(
//                paymentService.getPayment(id)
//        );
//    }
//
//    @PostMapping("/{id}/refund")
//    public ResponseEntity<PaymentResponse> refundPayment(
//            @PathVariable UUID id) {
//
//        return ResponseEntity.ok(
//                paymentService.refundPayment(id)
//        );
//    }
//}