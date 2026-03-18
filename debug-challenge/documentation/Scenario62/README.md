# Scenario 62: Ignoring vs Permitting All

The performance and security trade-off between bypassing the filter chain and allowing public access.

## Concept
- **`web.ignoring()`**: The request skips all security filters entirely. Faster because there's less overhead, but you lose all protection.
- **`http.permitAll()`**: The request goes through all filters (Headers, CSP, CSRF, JWT) but authorization is bypassed.

## Implementation Details
- **Ignoring**: Added `/api/scenario62/ignored` to `WebSecurityCustomizer`.
- **Permitting**: Added `/api/scenario62/permitted` to `SecurityFilterChain`.

### Configuration Snippet:
```java
// Ignoring
return (web) -> web.ignoring().requestMatchers("/api/scenario62/ignored");

// Permitting
.requestMatchers("/api/scenario62/permitted").permitAll()
```

## Verification Results (Header Analysis)
1. **Permitted Endpoint**: Check response headers via `curl -v`.
   - **Result**: `Content-Security-Policy`, `X-Frame-Options`, and `X-Content-Type-Options` are **PRESENT**.
2. **Ignored Endpoint**: Check response headers via `curl -v`.
   - **Result**: ALL security headers are **MISSING**.

## Interview Theory: Which one to use?
- **Static Assets ONLY**: Only use `ignoring()` for images, CSS, and JS files. These don't need security headers or CSRF protection.
- **Public APIs**: Use `permitAll()` for Login, Register, or public data endpoints. Even if they are public, you still want the browser to be protected by CSP and XSS headers.
- **The CORS Trap**: If you ignore a URL, the `CorsFilter` is also skipped. This will cause CORS errors on your frontend even if your CORS config is correct!
