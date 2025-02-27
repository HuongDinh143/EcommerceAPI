package com.ra.validate;

import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryValidate implements ConstraintValidator<CategoryUnique, String> {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !categoryRepository.existsByCatName(value);
    }
}
