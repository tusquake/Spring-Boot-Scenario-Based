# Global Exception Handling — Complete Interview Reference

## Table of Contents
1. [Introduction to Exception Handling](#1-introduction)
2. [@ExceptionHandler: Method-level Handling](#2-exception-handler)
3. [@ControllerAdvice: Global Handling](#3-controller-advice)
4. [Custom Error Responses (ErrorDTO)](#4-custom-responses)
5. [The Classic Interview Trap: Exception Hierarchy](#5-the-classic-interview-trap-hierarchy)
6. [Handling Validation Errors (MethodArgumentNotValidException)](#6-validation-errors)
7. [DefaultHandlerExceptionResolver](#7-default-resolver)
8. [ResponseStatusException](#8-response-status)
9. [Error Attributes in Spring Boot](#9-error-attributes)
10. [Handling Errors in Async Methods](#10-async-errors)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
A robust application should never return a raw stack trace to the user. Global exception handling allows you to catch errors and return a consistent, user-friendly JSON structure and the appropriate HTTP status code.

---

## 2. @ExceptionHandler
This annotation is used within a Controller to handle specific exceptions thrown by any of its handler methods.
```java
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<String> handleNotFound() {
    return ResponseEntity.status(404).body("User not found!");
}
```

---

## 3. @ControllerAdvice
A specialized `@Component` that acts as an "Interceptor of Exceptions" for the whole application. It consolidates all `@ExceptionHandler` methods into one central place.

---

## 4. Custom Error Responses
Instead of returning a String, you should return a standard object:
```json
{
  "timestamp": "2023-10-25T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User with ID 5 does not exist",
  "path": "/api/users/5"
}
```

---

## 5. The Classic Interview Trap: Specificity
**The Trap**: You have a handler for `RuntimeException` and a handler for `NullPointerException`.
**The Question**: Which one runs when a `NullPointerException` is thrown?
**The Answer**: Spring always picks the **most specific** handler. So the NPE handler will run. However, if you only had a `RuntimeException` handler, it would catch the NPE because NPE extends RuntimeException.

---

## 6. Validation Errors
When using `@Valid`, Spring throws a `MethodArgumentNotValidException`. To return a nice error list (e.g., "Email is invalid", "Password too short"), you must override the handler for this specific exception in your `@ControllerAdvice`.

---

## 7. DefaultHandlerExceptionResolver
This is a built-in Spring bean that automatically maps standard Spring exceptions (like `HttpRequestMethodNotSupportedException`) to the correct HTTP status codes (like 405 Method Not Allowed).

---

## 8. ResponseStatusException
An alternative to custom exceptions. You can throw it directly from your service:
`throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");`
It's quick but less reusable than custom exceptions + ControllerAdvice.

---

## 9. Error Attributes
You can customize the default Spring Boot error JSON by extending `DefaultErrorAttributes`. This allows you to add custom fields like `traceId` or `serviceName` to every error response globally.

---

## 10. Async Errors
Exceptions thrown in `@Async` methods cannot be caught by `@ControllerAdvice` because they run on a different thread. You must implement `AsyncUncaughtExceptionHandler` to handle them.

---

## 11. Common Mistakes
1. Catching `Exception.class` and returning a 200 OK with an "error" message inside. (Always use the correct HTTP status code).
2. Forgetting to log the stack trace in the `@ControllerAdvice` (makes debugging impossible).
3. Sending internal error messages (like SQL syntax errors) to the client.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I have multiple @ControllerAdvice classes?**  
A: Yes, you can use `@Order` to decide which one takes precedence.  
**Q: Does @ControllerAdvice catch errors from Filters?**  
A: No. Filters run before the `DispatcherServlet`, so they are outside the scope of `@ControllerAdvice`. You must handle Filter errors manually or using a custom ErrorController.
