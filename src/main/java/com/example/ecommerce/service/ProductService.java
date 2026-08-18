package com.example.ecommerce.service;

import com.example.ecommerce.dto.product.ProductRequest;
import com.example.ecommerce.dto.product.ProductResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceAlreadyExistsException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductResponse createProduct(
            ProductRequest request) throws RuntimeException{

        if (productRepository.existsBySku(request.sku())) {
            throw new ResourceAlreadyExistsException(
                    "SKU already exists"
            );
        }

        Category category =
                categoryRepository.findById(
                        request.categoryId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        ));

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .sku(request.sku())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(category)
                .active(true)
                .build();

        return mapToResponse(
                productRepository.save(product)
        );
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {

        return mapToResponse(
                getProductEntity(id)
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(
            String search,
            UUID categoryId,
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {

        BigDecimal min =
                minPrice == null
                        ? null
                        : BigDecimal.valueOf(minPrice);

        BigDecimal max =
                maxPrice == null
                        ? null
                        : BigDecimal.valueOf(maxPrice);

        return productRepository.searchProducts(
                        search,
                        categoryId,
                        min,
                        max,
                        pageable
                )
                .map(this::mapToResponse);
    }

    public ProductResponse updateProduct(
            UUID id,
            ProductRequest request) {

        Product product = getProductEntity(id);

        Category category =
                categoryRepository.findById(
                        request.categoryId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        ));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(
                request.stockQuantity()
        );
        product.setCategory(category);

        return mapToResponse(product);
    }

    public void deleteProduct(UUID id) throws RuntimeException{

        try {
            Product product = getProductEntity(id);

            product.setActive(false);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public ProductResponse updateStock(
            UUID id,
            Integer quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative"
            );
        }

        Product product = getProductEntity(id);

        product.setStockQuantity(quantity);

        return mapToResponse(product);
    }

    private Product getProductEntity(UUID id) throws RuntimeException {

        Product product =  productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: " + id
                        ));
        if(!product.isActive())
        {
            throw new ResourceNotFoundException("Product not found: "+id);
        }
        return product;
    }

    private ProductResponse mapToResponse(
            Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isActive(),
                product.getCategory().getId()
        );
    }
}