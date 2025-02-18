package com.ra.model.dto;

import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingCartResponse {
    private Long id;
    private String  productName;
    private String userName;
    private int orderQuantity;
}
