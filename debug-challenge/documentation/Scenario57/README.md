# Scenario 57: Content Security Policy (CSP)

Preventing Cross-Site Scripting (XSS) and data injection attacks by restricting where scripts can be loaded from using dynamic nonces.

## Concept
CSP is a security header that tells the browser which sources of content (scripts, styles, images) are trusted. A **Nonce** (Number used once) is a unique, random string added to the CSP header and to `<script>` tags. The browser only executes scripts with a matching nonce.

## Implementation Details
We implemented a `CspNonceFilter` that:
1. **Generates** a secure random nonce for every single HTTP request.
2. **Injects** it into the request attributes for use by the UI layer.
3. **Sets** the `Content-Security-Policy` header in the response.

### Filter Code Snippet:
```java
String nonce = Base64.getEncoder().encodeToString(nonceBytes);
request.setAttribute("cspNonce", nonce);
response.setHeader("Content-Security-Policy", "script-src 'self' 'nonce-" + nonce + "'; object-src 'none';");
```

## Verification Results
- **Action**: Call any protected endpoint (e.g., `/api/scenario57/test`) and check response headers.
- **Result**: You will see a header similar to: 
  `Content-Security-Policy: script-src 'self' 'nonce-P5C+mKRDyZ8kFysOOpkzCw=='; object-src 'none';`
- **Dynamic Check**: Refresh the request multiple times; notice the nonce value changes every time, proving it's unique per request.

## Interview Theory: CSP & XSS
- **The Ultimate Defense**: CSP is considered the strongest modern defense against XSS. Even if an attacker manages to inject a `<script>` tag into your HTML, the browser will refuse to run it because the attacker cannot guess the secret nonce generated on the server for that specific request.
- **'self' vs Nonce**: Just using `script-src 'self'` prevents loading scripts from other domains, but it doesn't stop "Inline Scripts" (scripts written directly in the HTML). The Nonce specifically solves the inline script problem.
- **Filter Placement**: This filter must run early in the chain (before `HeaderWriterFilter` or as part of it) to ensure the header is always applied, even if later filters fail.
