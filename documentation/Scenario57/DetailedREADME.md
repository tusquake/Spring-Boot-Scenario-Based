# Content Security Policy (CSP) — Complete Interview Reference

## Table of Contents
1. [What is Content Security Policy (CSP)?](#1-what-is-csp)
2. [How CSP Prevents XSS (Cross-Site Scripting)](#2-how-it-prevents-xss)
3. [Core CSP Directives (script-src, style-src, etc.)](#3-core-directives)
4. [Using Nonces for Inline Scripts](#4-using-nonces)
5. [The Classic Interview Trap: 'unsafe-inline' and 'unsafe-eval'](#5-the-classic-interview-trap-unsafe)
6. [CSP Reporting (report-uri / report-to)](#6-csp-reporting)
7. [Content-Security-Policy-Report-Only Header](#7-report-only)
8. [Configuring CSP in Spring Security](#8-configuration)
9. [CSP vs Same-Origin Policy (SOP)](#9-csp-vs-sop)
10. [Performance Impact of CSP](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is CSP?
CSP is an added layer of security that helps to detect and mitigate certain types of attacks, including Cross-Site Scripting (XSS) and data injection attacks. It is implemented via an HTTP response header that tells the browser which sources of content are trusted.

---

## 2. How CSP Prevents XSS
By default, browsers execute any script they find in a page. CSP allows the server to say: "Only execute scripts from my own domain" or "Only execute scripts that have a specific secret token (nonce)." This blocks scripts injected by attackers.

---

## 3. Core CSP Directives
- `default-src`: The fallback for other directives.
- `script-src`: Defines valid sources for JavaScript.
- `style-src`: Defines valid sources for stylesheets.
- `img-src`: Defines valid sources for images.
- `frame-ancestors`: Prevents clickjacking by limiting where the page can be embedded.

---

## 4. Using Nonces
A **Nonce** (number used once) is a cryptographically strong random string generated for every request. You include the nonce in the CSP header and in every inline `<script>` tag. The browser will only run scripts whose nonce matches the header.

---

## 5. The Classic Interview Trap: 'unsafe-inline'
**The Trap**: You want to enable some legacy inline scripts, so you add `'unsafe-inline'` to your `script-src`.
**The Problem**: This effectively **disables** the core protection of CSP against XSS, as an attacker can now inject their own inline scripts which the browser will gladly execute.
**The Fix**: Use nonces or hashes for specific inline scripts instead of the global `unsafe-inline` flag.

---

## 6. CSP Reporting
CSP can be configured to send a JSON report to a specific URL whenever a policy violation occurs. This allows developers to monitor and fix policy issues in production.

---

## 7. Report-Only Mode
Using the `Content-Security-Policy-Report-Only` header allows you to test a new policy without actually blocking anything. The browser will only log violations to the console and send reports to your reporting URL.

---

## 8. Configuring CSP in Spring Security
```java
http.headers(headers -> headers
    .contentSecurityPolicy(csp -> csp
        .policyDirectives("script-src 'self' 'nonce-...'")
    )
);
```

---

## 9. CSP vs Same-Origin Policy
- **SOP**: Prevents a site from *reading* data from another site.
- **CSP**: Prevents a site from *executing* malicious content injected into its own pages.

---

## 10. Performance Impact
CSP has zero impact on network performance (it's just a small header). However, implementing it can be "expensive" in terms of developer time, as it requires auditing all external scripts and styles.

---

## 11. Common Mistakes
1. Using `'unsafe-inline'` or `'unsafe-eval'`.
2. Forgetting to allow CDNs (like Google Fonts or JQuery) in the policy.
3. Not using a unique nonce for every single request.

---

## 12. Quick-Fire Interview Q&A
**Q: Can CSP prevent data exfiltration?**  
A: Yes, by using the `connect-src` directive to limit where the browser can send data (e.g., via `fetch` or `XMLHttpRequest`).  
**Q: Does CSP replace the need for input sanitization?**  
A: No. CSP is a defense-in-depth measure. You should still sanitize inputs to prevent the underlying XSS vulnerability.
