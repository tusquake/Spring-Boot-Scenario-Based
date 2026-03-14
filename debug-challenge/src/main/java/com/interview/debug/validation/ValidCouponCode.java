package com.interview.debug.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * SCENARIO 43: CUSTOM VALIDATION ANNOTATION
 * This annotation will enforce a custom rule for coupon codes.
 */
@Documented
@Constraint(validatedBy = CouponCodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCouponCode {
    String message() default "Invalid Coupon Code! Must start with 'DISCOUNT_' and be at least 10 chars.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
