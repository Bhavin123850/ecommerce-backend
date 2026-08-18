//package com.example.ecommerce.controller;
//
//import com.example.ecommerce.dto.product.ProductImageRequest;
//import com.example.ecommerce.dto.product.ProductImageResponse;
//import com.example.ecommerce.service.ProductImageService;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/products/{productId}/images")
//@RequiredArgsConstructor
//public class ProductImageController {
//
//    private final ProductImageService productImageService;
//
//    @PostMapping
//    public ResponseEntity<ProductImageResponse> addImage(
//            @PathVariable UUID productId,
//            @Valid @RequestBody ProductImageRequest request) {
//
//        ProductImageResponse response =
//                productImageService.addImage(
//                        productId,
//                        request
//                );
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ProductImageResponse>> getImages(
//            @PathVariable UUID productId) {
//
//        return ResponseEntity.ok(
//                productImageService.getProductImages(productId)
//        );
//    }
//
//    @DeleteMapping("/{imageId}")
//    public ResponseEntity<Void> deleteImage(
//            @PathVariable UUID productId,
//            @PathVariable UUID imageId) {
//
//        productImageService.deleteImage(
//                productId,
//                imageId
//        );
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @PatchMapping("/{imageId}/primary")
//    public ResponseEntity<Void> setPrimaryImage(
//            @PathVariable UUID productId,
//            @PathVariable UUID imageId) {
//
//        productImageService.setPrimaryImage(
//                productId,
//                imageId
//        );
//
//        return ResponseEntity.noContent().build();
//    }
//}