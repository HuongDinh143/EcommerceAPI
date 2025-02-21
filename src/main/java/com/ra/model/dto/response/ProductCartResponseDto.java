package com.ra.model.dto.response;

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
    private Long quantity;
    private double totalPrice;
}
