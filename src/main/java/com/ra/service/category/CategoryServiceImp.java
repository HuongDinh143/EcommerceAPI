package com.ra.service.category;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDto;
import com.ra.model.dto.request.CategoryUpdateRequestDto;
import com.ra.model.dto.response.CategoryResponseDto;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::toDto).toList();
    }

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryResponseDto> responseDtos = categories.stream()
                .map(this::toDto).toList();

        return new PageImpl<>(responseDtos, pageable, responseDtos.size());
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Category not found"));
        return toDto(category);
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto requestDto) {
        Category category = Category.builder()
                .catName(requestDto.getCatName())
                .catDesc(requestDto.getCatDesc())
                .status(true)
                .build();
        categoryRepository.save(category);
        return toDto(category);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryUpdateRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Category not found"));
        boolean isUpdate = false;
        if (requestDto.getCatName() != null) {
            category.setCatName(requestDto.getCatName());
            isUpdate = true;
        }
        if (requestDto.getCatDesc() != null) {
            category.setCatDesc(requestDto.getCatDesc());
            isUpdate = true;
        }
        if (requestDto.getStatus() != null) {
            category.setStatus(requestDto.getStatus());
            isUpdate = true;
        }
        if (isUpdate) {
            categoryRepository.save(category);
        }
        return toDto(category);
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Category not found"));
        categoryRepository.delete(category);

    }

    private CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .catName(category.getCatName())
                .catDesc(category.getCatDesc())
                .status(category.getStatus())
                .build();
    }
}
