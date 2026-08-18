//package com.example.ecommerce.service;
//
//import com.example.ecommerce.dto.product.ProductImageRequest;
//import com.example.ecommerce.dto.product.ProductImageResponse;
//import com.example.ecommerce.entity.Product;
//import com.example.ecommerce.entity.ProductImage;
//import com.example.ecommerce.exception.ResourceNotFoundException;
//import com.example.ecommerce.repository.ProductImageRepository;
//import com.example.ecommerce.repository.ProductRepository;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ProductImageService {
//
//    private final ProductImageRepository imageRepository;
//    private final ProductRepository productRepository;
//
//    public ProductImageResponse addImage(
//            UUID productId,
//            ProductImageRequest request) {
//
//        Product product =
//                productRepository.findById(productId)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Product not found"
//                                ));
//
////        if (request.primary()) {
////            imageRepository.findByProductId(productId)
////                    .forEach(image ->
////                            image.setPrimary(false)
////                    );
////        }
//
//        ProductImage image = ProductImage.builder()
//                .product(product)
//                .imageUrl(request.imageUrl())
//                .primary(request.primary())
//                .displayOrder(request.displayOrder())
//                .build();
//
//        return mapToResponse(
//                imageRepository.save(image)
//        );
//    }
//
//    @Transactional(readOnly = true)
//    public List<ProductImageResponse> getProductImages(
//            UUID productId) {
//
//        if (!productRepository.existsById(productId)) {
//            throw new ResourceNotFoundException(
//                    "Product not found"
//            );
//        }
//
//        return imageRepository
//                .findByProductId(productId)
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
//
//    public void deleteImage(
//            UUID productId,
//            UUID imageId) {
//
//        ProductImage image =
//                imageRepository
//                        .findByIdAndProductId(
//                                imageId,
//                                productId
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Image not found"
//                                ));
//
//        imageRepository.delete(image);
//    }
//
//    public void setPrimaryImage(
//            UUID productId,
//            UUID imageId) {
//
//        ProductImage image =
//                imageRepository
//                        .findByIdAndProductId(
//                                imageId,
//                                productId
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Image not found"
//                                ));
//
//        imageRepository.findByProductId(productId)
//                .forEach(img ->
//                        img.setPrimary(false)
//                );
//
//        image.setPrimary(true);
//    }
//
//    private ProductImageResponse mapToResponse(
//            ProductImage image) {
//
//        return new ProductImageResponse(
//                image.getId(),
//                image.getImageUrl(),
//                image.isPrimary(),
//                image.getDisplayOrder()
//        );
//    }
//}