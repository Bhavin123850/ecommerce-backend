package com.example.ecommerce.service;

import com.example.ecommerce.dto.category.CategoryRequest;
import com.example.ecommerce.dto.category.CategoryResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.exception.ResourceAlreadyExistsException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse createCategory(
            CategoryRequest request) throws RuntimeException{

        if (categoryRepository
                .existsByNameIgnoreCase(request.name())) {

            throw new ResourceAlreadyExistsException(
                    "Category already exists"
            );
        }

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .active(request.active())
                .build();

        return mapToResponse(
                categoryRepository.save(category)
        );
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(UUID id) {

        try {
            return mapToResponse(getCategoryEntity(id));
        }
        catch(RuntimeException e)
        {
            throw e;
        }
    }

    public CategoryResponse updateCategory(
            UUID id,
            CategoryRequest request) throws RuntimeException{

        try {
            Category category = getCategoryEntity(id);

            category.setName(request.name());
            category.setDescription(request.description());
            category.setActive(request.active());
            category.setUpdatedAt(Instant.now());
            return mapToResponse(category);
        }
        catch(RuntimeException e)
        {
            throw e;
        }
    }

    public void deleteCategory(UUID id) {

        try {
            Category category = getCategoryEntity(id);
            category.setActive(false);
        }
        catch(RuntimeException e)
        {
            throw e;
        }
    }

    private Category getCategoryEntity(UUID id) throws RuntimeException{

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + id
                        ));
        if(!category.isActive())
        {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        return category;
    }

    private CategoryResponse mapToResponse(
            Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}