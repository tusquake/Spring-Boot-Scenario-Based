# Global Exception Handling — Complete Interview Reference

## Table of Contents
1. [Why Global Exception Handling?](#1-why-global-exception-handling)
2. [@ControllerAdvice vs @RestControllerAdvice](#2-controlleradvice-vs-restcontrolleradvice)
3. [@ExceptionHandler Annotation](#3-exceptionhandler)
4. [Customizing Error Responses (Error Details DTO)](#4-customizing-responses)
5. [The Classic Interview Trap: Exception Priority](#5-the-classic-interview-trap-priority)
6. [Handling Validation Errors (@Valid)](#6-handling-validation)
7. [ResponseStatusException vs Custom Exceptions](#7-responsestatus-vs-custom)
8. [The Role of ResponseEntityExceptionHandler](#8-responseentityexceptionhandler)
9. [Logging in Exception Handlers](#9-logging)
10. [Testing Error Scenarios](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Why Global Exception Handling?
It allows you to centralize error logic, ensuring every API endpoint returns a consistent JSON error format and appropriate HTTP status codes.

---

## 2. @ControllerAdvice vs @RestControllerAdvice
`@RestControllerAdvice` is a combination of `@ControllerAdvice` and `@ResponseBody`. It is the modern standard for REST APIs, ensuring all return values are serialized to JSON.

---

## 3. @ExceptionHandler Annotation
Used inside an advice class to define which exception type a method should handle. For example, `@ExceptionHandler(ResourceNotFoundException.class)`.

---

## 4. Customizing Error Responses (Error Details DTO)
Instead of a simple string, return a structured object containing `timestamp`, `status`, `error`, and `message` for better client-side integration.

---

## 5. The Classic Interview Trap: Exception Priority
**The Trap**: If you have a handler for `RuntimeException` and `NullPointerException`, Spring will always pick the most specific one (`NullPointerException`).

---

## 6. Handling Validation Errors (@Valid)
When `@Valid` fails, Spring throws `MethodArgumentNotValidException`. You should catch this in your advice to return a list of specific field errors to the frontend.

---

## 7. ResponseStatusException vs Custom Exceptions
`ResponseStatusException` is a quick way to throw errors with a status code without creating custom exception classes.

---

## 8. The Role of ResponseEntityExceptionHandler
A base class provided by Spring that you should extend to inherit default handling for standard Spring MVC exceptions like 404, 405, etc.

---

## 9. Logging in Exception Handlers
Always log the stack trace in your exception handler so you can debug the root cause in your log management system (like ELK).

---

## 10. Testing Error Scenarios
Use `MockMvc` to verify that your controller returns the expected JSON structure and status code when an exception is triggered.

---

## 11. Common Mistakes
1. Returning a "200 OK" status with an "error" field in the body (Violation of REST principles).
2. Leaking internal stack traces to the public API response.

---

## 12. Quick-Fire Interview Q&A
**Q: Can advice classes be restricted to specific packages?**  
A: Yes, using `@RestControllerAdvice(basePackages = "...")`.  
**Q: What is the difference between @ResponseStatus and @ExceptionHandler?**  
A: `@ResponseStatus` is for simple status codes; `@ExceptionHandler` is for complex logic and body customization.
