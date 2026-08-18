package com.example.ecommerce.controller;

import com.example.ecommerce.dto.category.CategoryRequest;
import com.example.ecommerce.dto.category.CategoryResponse;

import com.example.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        try {
            CategoryResponse response =
                    categoryService.createCategory(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(
            @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(
                    categoryService.getCategory(id)
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
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {
        try {
            return ResponseEntity.ok(
                    categoryService.updateCategory(id, request)
            );
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable UUID id) {

        try {
            categoryService.deleteCategory(id);

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