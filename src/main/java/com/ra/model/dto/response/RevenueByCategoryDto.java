package com.ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RevenueByCategoryDto {
    private Long categoryId;
    private String categoryName;
    private double totalRevenue;
}
