package com.ra.model.dto.response;

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
    private Long orderQuantity;
}
