package com.ra.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CategoryValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryUnique {
    String message() default "{CategoryUnique.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
