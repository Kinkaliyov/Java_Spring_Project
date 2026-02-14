package com.library.dea.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Price must be greater than 0";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};

}
