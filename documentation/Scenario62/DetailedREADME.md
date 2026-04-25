# permitAll() vs web.ignoring() — Complete Interview Reference

## Table of Contents
1. [Introduction to Request Ignoring](#1-introduction)
2. [How permitAll() Works](#2-permit-all)
3. [How web.ignoring() Works](#3-web-ignoring)
4. [Comparing the Two Approaches](#4-comparison)
5. [The Classic Interview Trap: Missing Security Headers](#5-the-classic-interview-trap-headers)
6. [Security Context in Ignored Requests](#6-security-context)
7. [Impact on Performance](#7-performance)
8. [When to Use permitAll()](#8-when-to-use-permitall)
9. [When to Use web.ignoring()](#9-when-to-use-ignoring)
10. [Handling Static Resources (JS, CSS, Images)](#10-static-resources)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
In Spring Security, there are two main ways to allow public access to an endpoint. While they might seem identical, they work at different levels of the Spring stack and have significant security implications.

---

## 2. How permitAll() Works
`permitAll()` is configured within the `HttpSecurity` filter chain.
- The request **goes through** all Spring Security filters (JWT, CSRF, Session).
- The `AuthorizationManager` simply says "yes" to everyone.
- **Result**: You still get security headers (CSP, HSTS) in the response.

---

## 3. How web.ignoring() Works
`web.ignoring()` is configured in the `WebSecurityCustomizer`.
- The request **bypasses** the Spring Security filter chain entirely.
- Spring Security is not even aware the request happened.
- **Result**: You do **NOT** get any security headers in the response.

---

## 4. Comparison Table
| Feature | permitAll() | web.ignoring() |
|---|---|---|
| **Filter Chain** | Included | Bypassed |
| **Security Headers** | Yes | No |
| **Performance** | Slightly Slower | Faster |
| **Context Access** | Yes | No |

---

## 5. The Classic Interview Trap: Missing Headers
**The Trap**: You use `web.ignoring().requestMatchers("/public/**")`.
**The Problem**: Your public pages are now vulnerable to clickjacking and XSS because the security headers (like `X-Frame-Options` and `Content-Security-Policy`) are never added to the response.
**The Fix**: Use `permitAll()` for public API endpoints and pages; use `web.ignoring()` only for static assets.

---

## 6. Security Context
If you use `web.ignoring()`, you cannot access the `SecurityContextHolder` inside your controller, even if the user is logged in. The context will be empty. With `permitAll()`, you can still see who the user is if they happen to provide a valid token.

---

## 7. Performance
`web.ignoring()` is faster because it skips the entire security overhead. For high-traffic static files (images, css), this performance gain is meaningful.

---

## 8. When to Use permitAll()
- Public API endpoints (`/api/v1/products`).
- Login and Registration pages.
- Health check endpoints (`/actuator/health`) if you want them to have security headers.

---

## 9. When to Use web.ignoring()
- Static resources (`/css/**`, `/js/**`, `/images/**`).
- Favicon.
- Swagger UI assets (only the assets, not the actual API docs).

---

## 10. Static Resources
Spring Boot 3+ automatically handles many static resource locations, but you should still explicitly ignore them in `WebSecurityCustomizer` for maximum efficiency.

---

## 11. Common Mistakes
1. Using `web.ignoring()` for the login endpoint (leaves it vulnerable to CSRF).
2. Using `permitAll()` for thousands of small static icons (adds unnecessary overhead).
3. Assuming `ignoring()` is just a faster version of `permitAll()`.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use CSRF protection with web.ignoring()?**  
A: No, because the CSRF filter is bypassed.  
**Q: Which one is more secure?**  
A: `permitAll()` is more secure because it applies default security headers to the response.
