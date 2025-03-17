package com.ra.controller;

import com.ra.model.dto.response.ApiResponse;
import com.ra.model.dto.response.CategoryResponseDto;
import com.ra.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ApiResponse<?> findAll() {
        List<CategoryResponseDto> categories = categoryService.findAll();
        return new ApiResponse<>(200, "Danh sach danh muc", categories);
    }
}
