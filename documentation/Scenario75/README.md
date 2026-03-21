# Scenario 75: Custom Validations in Spring MVC

## Overview
Standard JSR-380 annotations cover basic rules, but production apps often need complex, multi-field, or database-driven validation. This scenario demonstrates how to build reusable custom constraints.

## The 3-Step Process 🏗️

### 1. The Annotation
Define the constraint's name and its parameters. We created `@FieldsMatch` and `@UniqueEmail`.
```java
@Constraint(validatedBy = FieldsMatchValidator.class)
public @interface FieldsMatch { ... }
```

### 2. The Validator
Implement the `ConstraintValidator<Annotation, Type>` interface.
- **FieldsMatchValidator**: Uses reflection (`BeanWrapperImpl`) to compare two field values.
- **UniqueEmailValidator**: Injects a Spring Repository (`Scenario75UserRepository`) to verify uniqueness in the database.

### 3. Application
Apply the annotation directly to your DTO or Entity.
```java
@FieldsMatch(field = "password", fieldMatch = "confirmPassword")
public class UserRegistrationDTO { ... }
```

## Advanced Patterns 🚀
- **Internationalization (i18n)**: Messages are moved to `messages.properties` using the `{key}` syntax in the annotation.
- **Dependency Injection**: Validators are managed by Spring, allowing you to inject services or repositories for complex business rules.
- **Cross-Field Logic**: `@FieldsMatch` is applied at the **class level** because it needs access to multiple properties of the object.

## Interview Tips 💡
- **Why use custom validators instead of service-level checks?** It centralizes business rules, promotes reusability, and keeps controllers/services clean.
- **What is the `ConstraintValidatorContext` for?** It allows you to customize the error message or disable the default constraint violation to add specific errors to specific fields.
- **Can you use `@Autowired` in a `ConstraintValidator`?** Yes, as long as your validator is managed by Spring (which it is by default in Spring Boot).

## Testing the Scenario
1. **Successful Registration**: `POST /api/scenario75/register` with valid JSON.
2. **Password Mismatch**: Change `confirmPassword`.
3. **Admin Check**: Set `role` to `ADMIN` but leave `adminKey` empty.
4. **Duplicate Email**: Register twice with the same email.
