package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    boolean existsBySku(String sku);

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Product> findByCategoryId(
            UUID categoryId,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Product p
    WHERE
        LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:search, ''), '%'))
    AND
        (:categoryId IS NULL OR p.category.id = :categoryId)
    AND
        (:minPrice IS NULL OR p.price >= :minPrice)
    AND
        (:maxPrice IS NULL OR p.price <= :maxPrice)
    AND
        p.active = true
    """)
    Page<Product> searchProducts(
            @Param("search") String search,
            @Param("categoryId") UUID categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}