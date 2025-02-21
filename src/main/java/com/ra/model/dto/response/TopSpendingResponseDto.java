package com.ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopSpendingResponseDto {
    private Long userId;
    private String username;
    private Double totalPrice;
}
