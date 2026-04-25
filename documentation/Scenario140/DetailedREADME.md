# Security Error Handling (401 vs 403) — Complete Interview Reference

## Table of Contents
1. [Introduction to Security Errors](#1-introduction)
2. [401 Unauthorized vs 403 Forbidden](#2-401-vs-403)
3. [AuthenticationEntryPoint: Handling 401](#3-authentication-entry-point)
4. [AccessDeniedHandler: Handling 403](#4-access-denied-handler)
5. [The Classic Interview Trap: Filters vs ControllerAdvice](#5-the-classic-interview-trap-trap)
6. [Customizing the JSON Error Response](#6-custom-json)
7. [Handling InsufficientAuthenticationException](#7-insufficient-auth)
8. [Security Errors in SPA (Single Page Applications)](#8-spa-errors)
9. [Integration with Global Exception Handlers](#9-integration)
10. [Logging and Auditing Security Failures](#10-auditing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
By default, Spring Security might redirect a user to a login page (for 401) or a generic error page (for 403). In a REST API, this is undesirable; we want a structured JSON response and the correct status code.

---

## 2. 401 vs 403
- **401 Unauthorized**: "I don't know who you are." (Authentication failed or missing).
- **403 Forbidden**: "I know who you are, but you don't have permission for this." (Authorization failed).

---

## 3. AuthenticationEntryPoint
This interface is the strategy used by `ExceptionTranslationFilter` to commence an authentication scheme. It is called when an unauthenticated user tries to access a protected resource.
**Goal**: Return a 401 JSON body.

---

## 4. AccessDeniedHandler
This interface is called when an authenticated user attempts to access a resource for which they do not have the required authorities (e.g., a USER trying to access `/admin`).
**Goal**: Return a 403 JSON body.

---

## 5. The Classic Interview Trap: Catching Security Errors
**The Trap**: A user says, *"I added a @ExceptionHandler(AccessDeniedException.class) to my @ControllerAdvice, but it's not working!"*
**The Problem**: Security filters run **before** the `DispatcherServlet`. Exceptions thrown in the filter chain (like those from `FilterSecurityInterceptor`) happen before Spring MVC even knows about the request.
**The Fix**: You must register your custom handlers in the `SecurityFilterChain` configuration, not just in a `@ControllerAdvice`.

---

## 6. Custom JSON Response
Instead of letting the browser return its default 401 page, you use an `ObjectMapper` inside your handler to write a clean JSON object to the `response.getOutputStream()`.

---

## 7. InsufficientAuthenticationException
This specific exception is thrown when a user is authenticated (e.g., via "Remember-Me") but the resource requires a "Fully Authenticated" status (e.g., for changing a password).

---

## 8. SPA Errors
For React/Angular apps, a 302 Redirect to a login page is a nightmare. Custom handlers ensure the frontend receives a 401, which it can then use to trigger a login modal or redirect the user programmatically.

---

## 9. Global Integration
While Security exceptions are handled in filters, some exceptions (like those from `@PreAuthorize`) **can** be caught by `@ControllerAdvice` because they happen inside the Controller. It is best practice to have both: custom handlers for filter-level errors and `@ExceptionHandler` for method-level security errors.

---

## 10. Auditing
Every 401 and 403 should be logged with the requesting IP, timestamp, and attempted resource. This is critical for detecting brute-force attacks or scraping attempts.

---

## 11. Common Mistakes
1. Confusing 401 and 403 in the response body.
2. Not setting the `Content-Type: application/json` header in the custom handler.
3. Forgetting to register the handlers in the `http.exceptionHandling()` config block.

---

## 12. Quick-Fire Interview Q&A
**Q: When is AuthenticationEntryPoint triggered?**  
A: When an `AuthenticationException` or an `AccessDeniedException` (for an anonymous user) is thrown.  
**Q: Can I have different handlers for different URL patterns?**  
A: Yes, by defining multiple `SecurityFilterChain` beans with different `securityMatcher` patterns.
