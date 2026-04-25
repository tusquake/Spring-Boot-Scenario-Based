# CORS (Cross-Origin Resource Sharing) — Complete Interview Reference

## Table of Contents
1. [What is CORS?](#1-what-is-cors)
2. [Same-Origin Policy (SOP)](#2-sop)
3. [Simple Requests vs Preflight Requests](#3-simple-vs-preflight)
4. [The OPTIONS Method](#4-options-method)
5. [The Classic Interview Trap: CORS vs Spring Security](#5-the-classic-interview-trap-security)
6. [Enabling CORS with @CrossOrigin](#6-cross-origin-annotation)
7. [Global CORS Configuration (WebMvcConfigurer)](#7-global-config)
8. [CORS Headers (Access-Control-Allow-*)](#8-cors-headers)
9. [Handling Credentials (Cookies/Auth Headers)](#9-credentials)
10. [CORS in a Microservices Gateway](#10-gateway)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is CORS?
CORS is a browser security feature that allows a web application running at one origin (domain) to access resources from a different origin. It uses HTTP headers to tell the browser if a cross-origin request is allowed.

---

## 2. Same-Origin Policy (SOP)
The browser's default behavior is to block scripts from reading data from a different domain (e.g., `app.com` cannot read data from `api.com` via AJAX). CORS is the "exception" to this rule.

---

## 3. Simple vs Preflight Requests
- **Simple**: Certain requests (GET/POST with standard headers) are sent directly.
- **Preflight**: For "risky" requests (PUT/DELETE or custom headers), the browser first sends an `OPTIONS` request to check if the server permits the operation.

---

## 4. The OPTIONS Method
The browser automatically sends an `OPTIONS` request before the actual request. The server must respond with a `200 OK` and the appropriate `Access-Control-Allow-Methods` and `Access-Control-Allow-Origin` headers.

---

## 5. The Classic Interview Trap: CORS vs Spring Security
**The Trap**: You configured CORS in `WebMvcConfigurer`, but requests are still being blocked with a `403 Forbidden`.
**The Problem**: Spring Security's filter chain runs **BEFORE** the MVC CORS logic. If the `OPTIONS` request is blocked by security, the actual request will never happen.
**The Fix**: You must configure CORS explicitly in the `HttpSecurity` filter chain using `http.cors()`.

---

## 6. Enabling CORS with @CrossOrigin
You can apply CORS rules to a specific controller or method using the `@CrossOrigin` annotation:
`@CrossOrigin(origins = "http://localhost:3000")`

---

## 7. Global CORS Configuration
For a cleaner approach, implement `WebMvcConfigurer` to define global rules for the entire application.

---

## 8. CORS Headers
- `Access-Control-Allow-Origin`: Which domains can access.
- `Access-Control-Allow-Methods`: Which HTTP methods (GET, POST, etc.) are allowed.
- `Access-Control-Allow-Headers`: Which custom headers can be sent.

---

## 9. Handling Credentials
If your app uses cookies or `Authorization` headers, you must set `allowCredentials = true`. **Warning**: When credentials are allowed, `allowedOrigins` cannot be `*` (the wildcard); it must be a specific domain.

---

## 10. CORS in a Microservices Gateway
In a microservices architecture, it is best to handle CORS at the **API Gateway** level so that individual services don't have to deal with it.

---

## 11. Common Mistakes
1. Using `allowedOrigins = "*"` in production (Security risk).
2. Forgetting to allow the `OPTIONS` method in Spring Security.
3. Not allowing custom headers (like `X-Correlation-ID`) in the CORS config.

---

## 12. Quick-Fire Interview Q&A
**Q: Is CORS a server-side or client-side security feature?**  
A: It is enforced by the **Browser** (client-side), but configured by the **Server**.  
**Q: Does CORS protect against CSRF?**  
A: No. CORS only prevents a site from *reading* the response. CSRF is about *performing* an action.
