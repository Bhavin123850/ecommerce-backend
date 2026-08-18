package com.example.ecommerce.service;

import com.example.ecommerce.dto.user.UpdateUserRequest;
import com.example.ecommerce.dto.user.UserResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) throws RuntimeException{

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id
                        ));

        return mapToResponse(user);
    }

    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request) throws RuntimeException{

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id
                        ));


        user.setName(request.name());
        user.setEmail(request.email());

        // Hash password
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setPhone(request.phone());
        user.setUpdatedAt(Instant.now());
        // Always CUSTOMER during registration

//        userRepository.save(user);


        return mapToResponse(user);
    }

    public void deleteUser(UUID id) throws RuntimeException{

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id
                        ));

        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.isEmailVerified(),
                user.getCreatedAt()
        );
    }
}
