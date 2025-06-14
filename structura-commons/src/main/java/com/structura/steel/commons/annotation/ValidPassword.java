package com.structura.steel.commons.annotation;

import com.structura.steel.commons.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must be at least 6 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character (!@#$%^&*()_+-=[]{}|;:,.<>?)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}