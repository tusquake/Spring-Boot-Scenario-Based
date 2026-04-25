# Enterprise HTTP Headers — Complete Interview Reference

## Table of Contents
1. [Standard vs Custom Headers](#1-standard-vs-custom)
2. [Observability: X-Correlation-ID](#2-observability)
3. [Proxying: X-Forwarded-For](#3-proxying)
4. [Rate Limiting Headers (X-RateLimit)](#4-rate-limiting)
5. [The Classic Interview Trap: Case Sensitivity](#5-the-classic-interview-trap-case-sensitivity)
6. [Security Headers (HSTS, CSP, X-Frame-Options)](#6-security-headers)
7. [Caching Headers (ETag, Cache-Control)](#7-caching)
8. [CORS Headers (Access-Control-Allow-Origin)](#8-cors)
9. [Setting Headers in Spring (ResponseEntity)](#9-setting-headers)
10. [Reading Headers with @RequestHeader](#10-reading-headers)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Standard vs Custom Headers
- **Standard**: Defined by IETF (e.g., `Content-Type`, `Authorization`, `Accept`).
- **Custom**: Application-specific, usually prefixed with `X-` (though the `X-` prefix is technically deprecated, it is still the industry standard for enterprise apps).

---

## 2. Observability: X-Correlation-ID
In a microservices world, a single request might pass through 10 services. The `X-Correlation-ID` (or `X-Request-ID`) is passed from the frontend and propagated through all services to allow end-to-end log tracing.

---

## 3. Proxying: X-Forwarded-For
When your app is behind a Load Balancer (Nginx, AWS ALB), the source IP of the request will be the Load Balancer's IP. The `X-Forwarded-For` header contains the actual client's IP.

---

## 4. Rate Limiting Headers
Servers often communicate rate limit status back to clients using these headers:
- `X-RateLimit-Limit`: Max requests allowed.
- `X-RateLimit-Remaining`: Remaining requests in current window.
- `X-RateLimit-Reset`: Time when the limit will reset.

---

## 5. The Classic Interview Trap: Case Sensitivity
**The Question**: *"Are HTTP Header names case-sensitive?"*
**The Answer**: **NO**. According to RFC 2616, header names are case-insensitive. `x-correlation-id` is the same as `X-Correlation-ID`. However, some poorly written parsers might fail, so it's best to follow standard capitalization.

---

## 6. Security Headers
Essential for protecting the frontend:
- **Strict-Transport-Security (HSTS)**: Force HTTPS.
- **X-Frame-Options**: Prevent Clickjacking.
- **Content-Security-Policy (CSP)**: Prevent XSS by restricting where scripts can be loaded from.

---

## 7. Caching Headers
- **Cache-Control**: `public`, `private`, `no-cache`, `max-age=3600`.
- **ETag**: A version identifier for a resource. If the ETag hasn't changed, the server returns `304 Not Modified`.

---

## 8. CORS Headers
Cross-Origin Resource Sharing (CORS) uses headers like `Access-Control-Allow-Origin` to tell the browser if a web app from one domain is allowed to access resources from another domain.

---

## 9. Setting Headers in Spring
Use `ResponseEntity` to return custom headers:
```java
return ResponseEntity.ok()
        .header("My-Header", "Value")
        .body(data);
```

---

## 10. Reading Headers
Use the `@RequestHeader` annotation in your controller:
```java
public String myMethod(@RequestHeader("User-Agent") String agent) { ... }
```

---

## 11. Common Mistakes
1. Trusting the client-side `User-Agent` (it can be easily spoofed).
2. Forgetting to propagate `X-Correlation-ID` when calling downstream microservices.
3. Not setting security headers, leaving the app vulnerable to basic web attacks.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the purpose of the 'Vary' header?**  
A: It tells caches which request headers (like `Accept-Encoding`) should be used to decide if a cached response can be sent.  
**Q: How do you handle large headers in Tomcat?**  
A: You may need to increase `server.max-http-header-size` if you are sending large JWT tokens or custom metadata.
