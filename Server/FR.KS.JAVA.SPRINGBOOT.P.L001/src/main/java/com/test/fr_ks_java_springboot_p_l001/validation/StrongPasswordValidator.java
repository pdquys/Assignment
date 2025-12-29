package com.test.fr_ks_java_springboot_p_l001.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return false;

        // >= 8 chars, 1 uppercase, 1 number
        return value.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
