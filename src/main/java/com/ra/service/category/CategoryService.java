package com.ra.service.category;

import com.ra.model.dto.CategoryResponseDto;
import com.ra.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();
}
