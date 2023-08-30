package com.matiej.springsecstudy.global.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingPasswordValidator.class)
@Documented
public @interface MatchingPassword {
    String message() default "Password doesn't match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
