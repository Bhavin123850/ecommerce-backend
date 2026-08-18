package com.example.ecommerce.controller;

import com.example.ecommerce.dto.product.ProductImageRequest;
import com.example.ecommerce.dto.product.ProductImageResponse;
import com.example.ecommerce.service.ProductImageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/images")
@RequiredArgsConstructor

public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<?> addImage(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductImageRequest request) {
        try {
            ProductImageResponse response =
                    productImageService.addImage(
                            productId,
                            request
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        catch(Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getImages(
            @PathVariable UUID productId,
            @PageableDefault(size=2) Pageable pageable) {
        try {
            return ResponseEntity.ok(
                    productImageService.getProductImages(productId,pageable)
            );
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        try {
            productImageService.deleteImage(
                    productId,
                    imageId
            );
            return ResponseEntity.noContent().build();
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{imageId}/primary")
    public ResponseEntity<?> setPrimaryImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {

        try {
            productImageService.setPrimaryImage(
                    productId,
                    imageId
            );
            return ResponseEntity.noContent().build();
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}