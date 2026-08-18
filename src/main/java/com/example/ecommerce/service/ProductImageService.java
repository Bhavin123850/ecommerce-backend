package com.example.ecommerce.service;

import com.example.ecommerce.dto.product.ProductImageRequest;
import com.example.ecommerce.dto.product.ProductImageResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {

    @Autowired
    private ProductImageRepository imageRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductImageResponse addImage(
            UUID productId,
            ProductImageRequest request) throws RuntimeException{

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        if (request.primary()) {
            imageRepository.findByProductId(productId)
                    .forEach(image ->
                            image.setPrimary(false)
                    );
        }

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.imageUrl())
                .primary(request.primary())
                .build();

        return mapToResponse(
                imageRepository.save(image)
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductImageResponse> getProductImages(
            UUID productId, Pageable pageable) throws RuntimeException{

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found"
            );
        }

        return imageRepository
                .findByProductId(productId, pageable)
                .map(this::mapToResponse);
    }

    public void deleteImage(
            UUID productId,
            UUID imageId) throws RuntimeException{

        ProductImage image =
                imageRepository
                        .findByIdAndProductId(
                                imageId,
                                productId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Image not found"
                                ));

        imageRepository.delete(image);
    }

    public void setPrimaryImage(
            UUID productId,
            UUID imageId) {

        ProductImage image =
                imageRepository
                        .findByIdAndProductId(
                                imageId,
                                productId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Image not found"
                                ));

        imageRepository.findByProductId(productId)
                .forEach(img ->
                        img.setPrimary(false)
                );

        image.setPrimary(true);
    }

    private ProductImageResponse mapToResponse(
            ProductImage image) {

        return new ProductImageResponse(
                image.getId(),
                image.getProduct().getId(),
                image.getImageUrl(),
                image.isPrimary(),
                image.getCreatedAt()
        );
    }
}