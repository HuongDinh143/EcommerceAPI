package com.ra.model.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponseDto {
    private Long id;
    private String catName;
    private String catDesc;
    private Boolean status;
}
