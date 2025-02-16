package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sku")
    private String sku = UUID.randomUUID().toString();
    @Column(name = "product_name")
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "unit_price")
    private Double unitPrice;
    @Column(name = "stock_quantity")
    private int stockQuantity;
    @Column(name = "image")
    private String image;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @ManyToOne
    @JoinColumn(name = "cat_id",referencedColumnName = "id")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails = new HashSet<>();
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<WishList> wishLists = new HashSet<>();

}
