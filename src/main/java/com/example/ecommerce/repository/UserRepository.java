package com.example.ecommerce.repository;

import com.example.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    @Modifying
    @Query("""
        UPDATE User u
        SET u.lastLoginAt = :lastLoginAt
        WHERE u.id = :userId
    """)
    int updateLastLoginAt(
            @Param("userId") UUID userId,
            @Param("lastLoginAt") Instant lastLoginAt
    );
}