package com.ra.model.dto;

import com.ra.validate.ProductUnique;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductCreateRequestDto {
    private String sku;
    @NotBlank
    @ProductUnique(message = "Tên sản phẩm đã tồn tại")
    private String productName;
    private String description;
    private Double unitPrice;
    private int stockQuantity;
    private String image;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
