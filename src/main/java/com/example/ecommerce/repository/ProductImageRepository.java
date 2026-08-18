package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ProductImage;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, UUID> {

    Page<ProductImage> findByProductId(UUID productId, Pageable pageable);

    List<ProductImage> findByProductId(UUID productId);
    Optional<ProductImage> findByIdAndProductId(
            UUID id,
            UUID productId
    );
}