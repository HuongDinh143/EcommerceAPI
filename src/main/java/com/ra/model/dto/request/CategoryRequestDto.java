package com.ra.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRequestDto {
    private String catName;
    private String catDesc;
    private Boolean status;
}
