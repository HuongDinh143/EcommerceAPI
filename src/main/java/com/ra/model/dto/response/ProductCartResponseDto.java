package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCartResponseDto {
    private Long productId;
    private String name;
    private String description;
    private double price;
    private Long quantity;
    private double totalPrice;
}
