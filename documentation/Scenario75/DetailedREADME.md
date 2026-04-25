# Validation Errors & BindingResult — Complete Interview Reference

## Table of Contents
1. [What is BindingResult?](#1-what-is-bindingresult)
2. [@Valid vs @Validated](#2-valid-vs-validated)
3. [Field Errors vs Global Errors](#3-field-vs-global)
4. [The Order of Arguments (Critical!)](#4-order-of-arguments)
5. [The Classic Interview Trap: Missing BindingResult](#5-the-classic-interview-trap-missing)
6. [Manual Error Injection with rejectValue](#6-manual-errors)
7. [Customizing Error Messages (Resource Bundles)](#7-customizing-messages)
8. [Formatting Errors for JSON Responses](#8-json-formatting)
9. [Validation Groups](#9-validation-groups)
10. [Global Exception Handling (MethodArgumentNotValidException)](#10-global-handler)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is BindingResult?
`BindingResult` is a Spring interface that holds the results of a validation and binding process. It contains any errors that occurred when Spring tried to map the request data (JSON/Form) to your Java object.

---

## 2. @Valid vs @Validated
- **@Valid**: Standard JSR-303 annotation. Used for basic validation.
- **@Validated**: Spring-specific annotation. Supports **Validation Groups** (e.g., different rules for `Create` vs `Update`).

---

## 3. Field vs Global Errors
- **Field Error**: Associated with a specific field (e.g., "Email is invalid").
- **Global Error**: Associated with the whole object (e.g., "Password and Confirm Password do not match").

---

## 4. The Order of Arguments
**CRITICAL**: The `BindingResult` argument MUST immediately follow the object being validated in the controller method signature.
```java
public String save(@Valid User user, BindingResult result) { ... }
```
If you put another argument (like `Model` or `HttpServletRequest`) between them, Spring won't populate the result correctly.

---

## 5. The Classic Interview Trap: Exception vs Result
**The Question**: *"What happens if you have @Valid but NO BindingResult in your method signature?"*
**The Answer**: If validation fails, Spring will throw a `MethodArgumentNotValidException` and the code inside your method will **NEVER** be executed. If you include `BindingResult`, your method **WILL** be called, and you must check `result.hasErrors()` manually.

---

## 6. Manual Error Injection
You can add your own custom business logic errors to the `BindingResult` using `rejectValue()`:
```java
if (database.exists(email)) {
    result.rejectValue("email", "exists", "Email is already taken");
}
```

---

## 7. Customizing Error Messages
Instead of hardcoding strings, use error codes (e.g., `Size.user.username`) and define the messages in `messages.properties`. This allows for easy internationalization (i18n).

---

## 8. JSON Formatting
In a REST API, you shouldn't just return `result.toString()`. You should map the `FieldError` objects into a clean JSON structure that the frontend can easily parse to show red labels under specific input boxes.

---

## 9. Validation Groups
Groups allow you to apply different sets of constraints. For example, the `id` field might be `@Null` during registration but `@NotNull` during an update.

---

## 10. Global Exception Handling
If you don't want to add `BindingResult` to every method, you can use a `@RestControllerAdvice` to catch `MethodArgumentNotValidException` globally and return a consistent error format for the whole app.

---

## 11. Common Mistakes
1. Forgetting to check `hasErrors()` inside the controller.
2. Placing `BindingResult` in the wrong position in the parameter list.
3. Not validating nested objects (you must add `@Valid` to the nested field too).

---

## 12. Quick-Fire Interview Q&A
**Q: Can BindingResult be used with @RequestParam?**  
A: No. It only works with model objects (like `@ModelAttribute` or `@RequestBody`).  
**Q: What is the difference between reject() and rejectValue()?**  
A: `reject()` creates a global error; `rejectValue()` creates an error for a specific field.
