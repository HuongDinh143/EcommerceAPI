package com.ra.model.dto.request;

import com.ra.validate.ProductUnique;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequestDto {
    private String sku;
    @NotBlank
    @ProductUnique(message = "Tên sản phẩm đã tồn tại")
    private String productName;
    private String description;
    private Double unitPrice;
    private Long stockQuantity;
    private MultipartFile image;
    private Long catId;
    private Boolean isFeatured;
}
