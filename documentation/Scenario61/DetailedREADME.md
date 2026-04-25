# Authentication vs Authorization (401 vs 403) — Complete Interview Reference

## Table of Contents
1. [Definitions: Authentication vs Authorization](#1-definitions)
2. [What is a 401 Unauthorized Error?](#2-401-error)
3. [What is a 403 Forbidden Error?](#3-403-error)
4. [The Role of AuthenticationEntryPoint](#4-entry-point)
5. [The Classic Interview Trap: 401 when it should be 403](#5-the-classic-interview-trap-trap)
6. [The Role of AccessDeniedHandler](#6-access-denied-handler)
7. [Spring Security Exception Translation Filter](#7-exception-translation)
8. [Debugging Security Exceptions](#8-debugging)
9. [Customizing Error Responses (JSON)](#9-customizing-responses)
10. [Security Exceptions in Single Page Apps (SPA)](#10-spa-exceptions)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Definitions
- **Authentication**: Who are you? (Identity).
- **Authorization**: What are you allowed to do? (Permissions).

---

## 2. 401 Unauthorized
This status code means the server does not know who the user is. It usually happens when:
- No token/credentials provided.
- Expired token.
- Invalid credentials.

---

## 3. 403 Forbidden
This status code means the server knows who you are, but you do not have permission to perform the requested action.
- A regular user trying to access an `/admin` endpoint.
- A user with `read` scope trying to perform a `write` operation.

---

## 4. AuthenticationEntryPoint
This interface is called when an unauthenticated user tries to access a protected resource. Its job is to "commence" the authentication process (e.g., redirect to login page or return a 401 JSON).

---

## 5. The Classic Interview Trap: 401 or 403?
**The Question**: *"What happens if a user is logged in as a USER and tries to access an ADMIN page? Does they get a 401 or 403?"*
**The Answer**: They get a **403 Forbidden**. They are authenticated, but not authorized. If the server returned a 401, the browser might try to show a login popup, which is incorrect because the user is already logged in.

---

## 6. AccessDeniedHandler
This interface is called when an authenticated user lacks the required authorities. You can implement this to return a custom error message or log the unauthorized attempt.

---

## 7. Exception Translation Filter
This filter sits at the end of the Spring Security filter chain. It catches security exceptions (`AuthenticationException` and `AccessDeniedException`) and decides whether to trigger the `AuthenticationEntryPoint` or the `AccessDeniedHandler`.

---

## 8. Debugging Security Exceptions
Turn on `logging.level.org.springframework.security=DEBUG` to see exactly which filter or rule blocked a request.

---

## 9. Customizing JSON Responses
By default, Spring might return an HTML error page. For APIs, you should configure a custom `AuthenticationEntryPoint` and `AccessDeniedHandler` to return a standardized JSON error object.

---

## 10. Security in SPAs
In a React/Angular app, the frontend should intercept 401 responses and redirect the user to the login page, while 403 responses should show a "Permission Denied" notification.

---

## 11. Common Mistakes
1. Mixing up the implementation of EntryPoint and AccessDeniedHandler.
2. Returning a 403 for an unauthenticated user (Security risk: reveals that the resource exists).
3. Not handling security exceptions in `@RestControllerAdvice` (Security exceptions are often handled before they reach the controller layer).

---

## 12. Quick-Fire Interview Q&A
**Q: Is a missing JWT a 401 or 403?**  
A: 401 Unauthorized.  
**Q: Can I return a 404 instead of a 403 for security reasons?**  
A: Yes, this is common to avoid "leaking" the existence of sensitive admin URLs to regular users.
