package com.example.ecommerce.service;

import com.example.ecommerce.dto.auth.AuthResponse;
import com.example.ecommerce.dto.auth.LoginRequest;
import com.example.ecommerce.dto.auth.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.UserRole;
import com.example.ecommerce.exception.EmailAlreadyRegisteredException;
import com.example.ecommerce.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private com.example.ecommerce.service.JwtService jwtService;
//    @Autowired
//    private User user;

//    public AuthService(
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder,
//            AuthenticationManager authenticationManager,
//            JwtService jwtService
//    ) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
//    }

    public void register(RegisterRequest request) throws RuntimeException {
        // Check whether email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());

        // Hash password
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setPhone(request.phone());

        // Always CUSTOMER during registration
        user.setRole(request.role());
//        user.setCreatedAt(Instant.now());
//        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) throws Exception{

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found")
                    );

            // Update last login time
            userRepository.updateLastLoginAt(
                    user.getId(),
                    Instant.now()
            );
            String token =
                    jwtService.generateToken(userDetails);

            return new AuthResponse(token);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");

        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found");
        }
    }
}