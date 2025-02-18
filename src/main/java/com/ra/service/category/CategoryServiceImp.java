package com.ra.service.category;

import com.ra.model.dto.CategoryResponseDto;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category ->
                CategoryResponseDto.builder()
                        .id(category.getId())
                        .catName(category.getCatName())
                        .catDesc(category.getCatDesc())
                        .status(category.getStatus())
                        .build()).toList();
    }
}
