# CSRF (Cross-Site Request Forgery) — Complete Interview Reference

## Table of Contents
1. [What is CSRF?](#1-what-is-csrf)
2. [How CSRF Attacks Work](#2-how-it-works)
3. [Stateless vs Stateful CSRF Protection](#3-stateless-vs-stateful)
4. [The Sync Token Pattern](#4-sync-token)
5. [The Classic Interview Trap: CSRF and REST APIs](#5-the-classic-interview-trap-rest-apis)
6. [Cookie-Based CSRF Protection (XSRF-TOKEN)](#6-cookie-based)
7. [Disabling CSRF (When is it safe?)](#7-disabling)
8. [CSRF vs XSS](#8-csrf-vs-xss)
9. [Configuring CSRF in Spring Security](#9-configuration)
10. [SameSite Cookie Attribute](#10-samesite)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is CSRF?
CSRF is an attack that forces an authenticated user to execute unwanted actions on a web application in which they are currently authenticated. It targets state-changing requests (like changing passwords or transferring money) by leveraging the browser's automatic inclusion of cookies.

---

## 2. How CSRF Attacks Work
1. You are logged into `bank.com`.
2. You visit `evil.com` in another tab.
3. `evil.com` contains a hidden form that submits a POST request to `bank.com/transfer`.
4. Your browser automatically attaches your session cookie to the request.
5. `bank.com` sees the cookie and processes the transfer.

---

## 3. Stateless vs Stateful CSRF
- **Stateful**: The server stores the token in the session and compares it with the one in the request.
- **Stateless (Double Submit Cookie)**: The server sends a random token in a cookie. The client reads this cookie and sends the same token in a custom header. The server just compares the cookie value with the header value.

---

## 4. The Sync Token Pattern
Spring Security's default mechanism. A unique token is generated for every session. For every POST/PUT/DELETE request, the client must include this token.

---

## 5. The Classic Interview Trap: REST APIs
**The Question**: *"Do we need CSRF protection for a REST API that uses JWT?"*
**The Answer**: **NO**, as long as the JWT is stored in **Local Storage** and not in a **Cookie**. CSRF only works against cookies. If you store your JWT in an `HttpOnly` cookie, you STILL need CSRF protection.

---

## 6. Cookie-Based CSRF Protection
For Single Page Applications (React/Angular), Spring can use `CookieCsrfTokenRepository`. It sends the token in a cookie named `XSRF-TOKEN`. The frontend reads it and sends it back in the `X-XSRF-TOKEN` header.

---

## 7. Disabling CSRF
You should only disable CSRF if:
1. You are building a purely stateless API (using JWT/Header-based auth).
2. The API is only consumed by non-browser clients (like mobile apps or other servers).

---

## 8. CSRF vs XSS
- **XSS**: Attacker injects a script into your site to steal data.
- **CSRF**: Attacker tricks your browser into performing an action on your behalf on *another* site.

---

## 9. Configuring CSRF in Spring Security
```java
http.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
);
```

---

## 10. SameSite Cookie Attribute
The `SameSite=Strict` or `Lax` attribute on cookies is a modern browser defense that prevents cookies from being sent in cross-site requests, effectively killing most CSRF attacks even without tokens.

---

## 11. Common Mistakes
1. Disabling CSRF for a session-based (cookie) application.
2. Not using `HttpOnly` for session cookies while using `HttpOnly=false` for CSRF cookies.
3. Forgetting that CSRF protection is enabled by default in Spring Security.

---

## 12. Quick-Fire Interview Q&A
**Q: Does CSRF protection apply to GET requests?**  
A: No. GET requests should be idempotent (safe) and should not change state.  
**Q: Why use a custom header for the token?**  
A: Custom headers cannot be sent across domains without a CORS preflight check, providing an extra layer of security.
