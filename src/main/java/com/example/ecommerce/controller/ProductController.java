package com.example.ecommerce.controller;

import com.example.ecommerce.dto.product.ProductRequest;
import com.example.ecommerce.dto.product.ProductResponse;

import com.example.ecommerce.exception.ResourceAlreadyExistsException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest request) {

        try {
            ProductResponse response =
                    productService.createProduct(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        catch(ResourceAlreadyExistsException e)
        {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
        catch(ResourceNotFoundException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 40)
            Pageable pageable) {
        return ResponseEntity.ok(
                productService.searchProducts(
                        search,
                        categoryId,
                        minPrice,
                        maxPrice,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(
            @PathVariable UUID id) {
    try {
        return ResponseEntity.ok(
                productService.getProduct(id)
        );
    }
    catch(RuntimeException e)
    {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable UUID id) {
        try {
            productService.deleteProduct(id);

            return ResponseEntity.noContent().build();
        }
        catch(Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                productService.updateStock(id, quantity)
        );
    }
}