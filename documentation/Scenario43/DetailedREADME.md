# Custom Bean Validation (@Constraint) — Complete Interview Reference

## Table of Contents
1. [Introduction to JSR-380 (Bean Validation)](#1-introduction)
2. [Why Create Custom Validators?](#2-why-custom-validators)
3. [The Two Parts: Annotation and Validator](#3-two-parts)
4. [Defining the @Constraint Annotation](#4-defining-annotation)
5. [Implementing the ConstraintValidator Class](#5-implementing-validator)
6. [The Classic Interview Trap: Dependency Injection in Validators](#6-the-classic-interview-trap-di)
7. [Validating Multiple Fields (Class-level Validators)](#7-class-level-validation)
8. [Customizing Error Messages (ValidationMessages.properties)](#8-custom-messages)
9. [Handling Validation Exceptions Globally](#9-global-exception-handling)
10. [Validation at the Service Layer](#10-service-layer)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to JSR-380
JSR-380 is the Java standard for bean validation (Bean Validation 2.0). It allows you to express constraints on your data using annotations.

---

## 2. Why Create Custom Validators?
Standard annotations like `@NotNull` or `@Email` are great, but real-world business logic often requires more:
- **Coupon Codes**: Must follow a specific pattern and exist in the DB.
- **Passwords**: Must contain complex combinations.
- **Start/End Dates**: End date must be after Start date.

---

## 3. The Two Parts of a Custom Validator
1. **Annotation**: The `@interface` that the developer puts on the field.
2. **Validator**: The Java class that contains the logic to check if the data is valid.

---

## 4. Defining the Annotation
Use the `@Constraint` annotation to link your interface to the implementation class.
```java
@Constraint(validatedBy = CouponCodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCouponCode {
    String message() default "Invalid coupon code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

---

## 5. Implementing ConstraintValidator
You must implement the `ConstraintValidator<AnnotationType, FieldType>` interface.
```java
public class CouponCodeValidator implements ConstraintValidator<ValidCouponCode, String> {
    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null) return true; // Let @NotNull handle nulls
        return code.startsWith("SAVE");
    }
}
```

---

## 6. The Classic Interview Trap: Dependency Injection
**The Question**: *"Can you use @Autowired inside a ConstraintValidator?"*
**The Answer**: **YES**. Since Spring 4.x, Spring automatically manages the lifecycle of validators, so you can inject Repositories or Services into your custom validator to perform database checks.

---

## 7. Class-level Validators
If you need to compare two fields (e.g., `password` and `confirmPassword`), you must apply the custom annotation at the **Class level** rather than the field level.

---

## 8. Customizing Error Messages
Instead of hardcoding messages, use placeholders like `{coupon.invalid}` and define the text in `src/main/resources/ValidationMessages.properties`.

---

## 9. Handling Validation Exceptions
When `@Valid` fails in a controller, Spring throws `MethodArgumentNotValidException`. You should catch this in your `@RestControllerAdvice` to return a clean JSON response to the client.

---

## 10. Validation at the Service Layer
By adding `@Validated` at the class level of a Service, Spring will trigger validation for method parameters, even if they aren't coming from a REST request.

---

## 11. Common Mistakes
1. Forgetting to include `groups()` and `payload()` in the annotation interface (this is required by the spec).
2. Not handling `null` values inside `isValid()` (usually, validators should return `true` for null and let `@NotNull` handle it).
3. Putting expensive database logic inside a validator without caching (can slow down every request).

---

## 12. Quick-Fire Interview Q&A
**Q: Can a single field have multiple custom validators?**  
A: Yes, validation is cumulative.  
**Q: What is the benefit of custom validation over a simple if-check in the service?**  
A: It promotes reuse and keeps your business logic clean and focused on "what to do" rather than "checking data."
