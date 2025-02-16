package com.ra.validate;

import com.ra.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductValidate implements ConstraintValidator<ProductUnique, String> {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return  !productRepository.existsByProductName(s);
    }
}
