package com.ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryUpdateRequestDto {
    private String catName;
    private String catDesc;
    private Boolean status;
}
