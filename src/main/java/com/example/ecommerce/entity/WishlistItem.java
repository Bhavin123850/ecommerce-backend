package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_wishlist_product",
                        columnNames = {"wishlist_id", "product_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_wishlist_items_wishlist_id",
                        columnList = "wishlist_id"
                ),
                @Index(
                        name = "idx_wishlist_items_product_id",
                        columnList = "product_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "wishlist_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_wishlist_item_wishlist"
            )
    )
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_wishlist_item_product"
            )
    )
    private Product product;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}