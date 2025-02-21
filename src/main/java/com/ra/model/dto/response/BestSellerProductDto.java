package com.ra.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BestSellerProductDto {
    private Long productId;
    private String productName;
    private Long totalSold;
}
