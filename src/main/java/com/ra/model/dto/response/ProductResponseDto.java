package com.ra.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String description;
    private Double unitPrice;
    private Long stockQuantity;
    private String image;
    private String catName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
