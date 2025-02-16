package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @Column(name = "order_quantity")
    private int orderQuantity;
}
