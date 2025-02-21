package com.ra.service.category;

import com.ra.model.dto.request.CategoryRequestDto;
import com.ra.model.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();
    Page<CategoryResponseDto> findAll(Pageable pageable);
    CategoryResponseDto findById(Long id);
    CategoryResponseDto create(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);
    void delete(Long id);
}
