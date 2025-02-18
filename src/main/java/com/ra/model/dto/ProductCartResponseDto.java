package com.ra.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductCartResponseDto {
    private Long productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private double totalPrice;
}
