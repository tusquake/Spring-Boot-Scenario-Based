# Scenario 55: CSRF Protection (Cross-Site Request Forgery)

Demonstrates how to prevent an attacker from tricking an authenticated user into performing unwanted actions on your site.

## Concept
If you are logged into your bank, a malicious site could have a hidden form that submits to `bank.com/transfer?to=hacker`. Since you have a cookie, the request succeeds. **CSRF Tokens** are unique "Keys" that must be sent with every POST request, proving it came from *your* actual site.

## Implementation Details
We configured Spring Security to use `CookieCsrfTokenRepository`, which makes it easy for JavaScript frontends (like Angular/React) to read the token.

### Security Config:
```java
.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
```

## Verification Results
1. **Get Token**: Call `/api/scenario55/token`. Spring sets an `XSRF-TOKEN` cookie in your browser.
2. **Post without Token**: Call `/api/scenario55/update`. -> Returns `403 Forbidden`.
3. **Post with Token**: Send the value from the cookie in the `X-XSRF-TOKEN` header. -> Returns `200 Success`.

## Interview Theory: CSRF vs Stateless
- **Stateless APIs**: If your app is truly stateless (no cookies, only JWT in headers), you are often immune to CSRF because browsers don't "auto-send" headers like they do with cookies.
- **Standard Header**: The standard header name for the token is `X-XSRF-TOKEN`.
- **Safe Methods**: Remind the interviewer that GET, HEAD, and OPTIONS requests should never require a CSRF token as they should be "Safe" (no side effects).
