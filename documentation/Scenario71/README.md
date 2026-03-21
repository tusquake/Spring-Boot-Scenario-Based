# Scenario 71: Clickjacking Protection (UI Redressing)

## Overview
**Clickjacking** (also known as "UI Redressing") is an attack where an attacker uses multiple transparent or opaque layers to trick a user into clicking a button or link on another page when they were intending to click on the top-level page.

For example, an attacker can embed your banking site inside an invisible `<iframe>` on their own malicious site. They then place a fake "Click here to win a prize!" button exactly over your banking site's "Transfer Funds" button.

## How to Prevent Clickjacking

### 1. X-Frame-Options (Legacy)
This HTTP response header tells the browser whether it is allowed to render a page in a `<frame>`, `<iframe>`, `<embed>` or `<object>`.
- **DENY**: No one can embed the page.
- **SAMEORIGIN**: Only pages on the same origin can embed it.
- **ALLOW-FROM uri**: (Deprecated/Non-standard) Only the specified URI can embed it.

### 2. Content-Security-Policy: frame-ancestors (Modern)
This is the modern replacement for `X-Frame-Options`. It provides more flexibility (e.g., allowing multiple specific domains).
- `Content-Security-Policy: frame-ancestors 'none';` (Same as DENY)
- `Content-Security-Policy: frame-ancestors 'self';` (Same as SAMEORIGIN)
- `Content-Security-Policy: frame-ancestors example.com;` (Allow specific domain)

## Implementation in Spring Security

In this scenario, we configure both for maximum compatibility:

```java
.headers(headers -> headers
    .frameOptions(frame -> frame.deny()) // X-Frame-Options: DENY
    .contentSecurityPolicy(csp -> csp
        .policyDirectives("frame-ancestors 'none';") // CSP Protection
    )
)
```

## Interview Tips 💡
- **Why use both?** `X-Frame-Options` is older and has better support in very old browsers (IE). `CSP frame-ancestors` is standard and more powerful (can allow multiple specific origins).
- **What is the difference between DENY and SAMEORIGIN?** `DENY` is absolute. `SAMEORIGIN` allows your own site to frame itself (common for complex portals).
- **Is Clickjacking possible without Iframes?** It primary relies on framing. Without the ability to frame your site, the attack vector is significantly reduced.

## Testing the Scenario
1. Open the [Sensitive Page](http://localhost:8080/debug-application/api/scenario71/sensitive-page).
2. Check the response headers in Browser DevTools (Network tab).
3. Observe the `X-Frame-Options: DENY` and `Content-Security-Policy: frame-ancestors 'none';` headers.
