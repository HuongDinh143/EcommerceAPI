package com.ra.model.dto.response;


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
