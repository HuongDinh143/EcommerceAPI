package com.ra.model.dto.request;

import com.ra.validate.CategoryUnique;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRequestDto {
    @CategoryUnique(message = "Tên danh mục đã tồn tại")
    private String catName;
    private String catDesc;
}
