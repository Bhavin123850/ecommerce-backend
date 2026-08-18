package com.example.ecommerce.controller;

import com.example.ecommerce.dto.user.UpdateUserRequest;
import com.example.ecommerce.dto.user.UserResponse;
import com.example.ecommerce.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable UUID id) {

        try {
            return ResponseEntity.ok(
                    userService.getUserById(id)
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
    public ResponseEntity<?> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            return ResponseEntity.ok(
                    userService.updateUser(id, request)
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
    public ResponseEntity<?> deleteUser(
            @PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}