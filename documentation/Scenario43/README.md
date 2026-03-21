# Scenario 43: Custom Validation Annotations

Demonstrates how to go beyond standard `@NotNull` and create your own business-specific validation logic.

## Concept
Generic annotations aren't enough for complex rules like "Coupon codes must start with 'SUMMER' and be 10 characters long." Custom validators keep your controller clean and your logic reusable.

## Implementation Details
We created a `@ValidCouponCode` annotation and a corresponding `CouponCodeValidator` class.

### Annotation Usage:
```java
public class CouponRequest {
    @ValidCouponCode
    private String code;
}
```

### Validator Logic:
```java
public boolean isValid(String code, ConstraintValidatorContext context) {
    return code != null && code.startsWith("SUMMER") && code.length() == 10;
}
```

## Verification Results
- **Invalid**: Send `code: "WINTER"`. -> Returns `400 Bad Request`.
- **Valid**: Send `code: "SUMMER2024"`. -> Returns `200 OK`.

## Interview Theory: Custom Validation
- **ConstraintValidator**: Mention that your validator class must implement the `ConstraintValidator<Annotation, Type>` interface.
- **Database Access**: You can actually `@Autowired` services into your validator (e.g., to check if a username already exists in the DB).
- **Client Side**: Remind the interviewer that while server-side validation is mandatory for security, you should still do client-side validation for a better User Experience.
