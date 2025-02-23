package com.ra.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponseDto {
    private Long id;
    private String productName;
    private Double unitPrice;
    private Long orderQuantity;
    private Double totalPrice;
}
