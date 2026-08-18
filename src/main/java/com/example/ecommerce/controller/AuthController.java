package com.example.ecommerce.controller;

import com.example.ecommerce.dto.auth.AuthResponse;
import com.example.ecommerce.dto.auth.LoginRequest;
import com.example.ecommerce.dto.auth.RegisterRequest;
import com.example.ecommerce.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            authService.register(request);
            return ResponseEntity.ok(
                    "User registered successfully"
            );
        }
        catch(RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    authService.login(request)
            );
        }
        catch (Exception e)
        {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}