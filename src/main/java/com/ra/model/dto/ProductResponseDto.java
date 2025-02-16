package com.ra.model.dto;

import jakarta.validation.constraints.NotBlank;
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
    private int stockQuantity;
    private String image;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
