package com.structura.steel.commons.validation;

import com.structura.steel.commons.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final int MIN_LENGTH = 6; // Use 6 if you prefer a shorter minimum length

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Allow null passwords (e.g., for optional password updates in UpdateUserRequest)
        if (password == null) {
            return true;
        }

        // Check minimum length
        if (password.length() < MIN_LENGTH) {
            return false;
        }

        // Check for uppercase, lowercase, digit, and special character
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (SPECIAL_CHARS.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
}
