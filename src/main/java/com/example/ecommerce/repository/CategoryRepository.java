package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository
        extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);
}