package com.interview.debug.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CouponCodeValidator implements ConstraintValidator<ValidCouponCode, String> {

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null) return false;
        
        // Custom Logic: Must start with DISCOUNT_ and be 10+ chars
        return code.startsWith("DISCOUNT_") && code.length() >= 10;
    }
}
