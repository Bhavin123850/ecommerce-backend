package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository
        extends JpaRepository<Address, UUID> {

    List<Address> findByUserId(UUID userId);

    Optional<Address> findByIdAndUserId(
            UUID id,
            UUID userId
    );

    void deleteByUserId(UUID userId);
}