# Content Security Policy (CSP) & Nonces — Complete Interview Reference

## Table of Contents
1. [What is Content Security Policy (CSP)?](#1-introduction)
2. [XSS Protection: The CSP Solution](#2-xss-protection)
3. [The Problem with 'unsafe-inline'](#3-unsafe-inline)
4. [What is a Nonce?](#4-what-is-a-nonce)
5. [Implementing CSP Nonce in Spring Security](#5-spring-implementation)
6. [The Classic Interview Trap: Nonce Persistence](#6-the-classic-interview-trap-trap)
7. [CSP Directives (script-src, object-src, frame-ancestors)](#7-directives)
8. [CSP Report-Only Mode](#8-report-only)
9. [Integrating Nonces with Thymeleaf/React](#9-integration)
10. [Handling Dynamic Scripts (Strict-Dynamic)](#10-strict-dynamic)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is CSP?
CSP is an HTTP response header that allows site administrators to declare approved sources of content that browsers are allowed to load on that page. It is the primary defense against Cross-Site Scripting (XSS).

---

## 2. XSS Protection
XSS happens when an attacker injects a malicious `<script>` into your page. CSP blocks this by telling the browser: "Only execute scripts from my-domain.com".

---

## 3. 'unsafe-inline'
Historically, developers used `script-src 'unsafe-inline'` to allow scripts written directly in HTML. This is dangerous because it also allows attacker-injected scripts. **NEVER** use `unsafe-inline` in production.

---

## 4. What is a Nonce?
A Nonce (Number used once) is a unique, cryptographically strong random string generated for **every single request**.
1. The server generates a nonce: `base64(random_bytes)`.
2. The server adds the nonce to the CSP header: `script-src 'nonce-XYZ'`.
3. The server adds the same nonce to valid script tags: `<script nonce="XYZ">...</script>`.
The browser will only execute scripts that have the matching nonce.

---

## 5. Spring Implementation
1. Create a `Filter` to generate the nonce and store it in `request.setAttribute("cspNonce", nonce)`.
2. Configure Spring Security to read this attribute and add it to the `Content-Security-Policy` header.

---

## 6. The Classic Interview Trap: Reuse
**The Trap**: A user says, *"I generate a nonce once when the user logs in and use it for the whole session."*
**The Problem**: If an attacker discovers the nonce once, they can use it to bypass CSP for the rest of the session.
**The Fix**: A nonce **MUST** be unique for every single HTTP request.

---

## 7. Key Directives
- **default-src**: Fallback for other directives.
- **script-src**: Where scripts can be loaded from.
- **object-src**: Restricts plugins like Flash (usually set to `'none'`).
- **frame-ancestors**: Prevents Clickjacking by restricting who can embed your page in an iframe.

---

## 8. Report-Only Mode
Instead of `Content-Security-Policy`, you can use `Content-Security-Policy-Report-Only`. This will not block anything but will send a JSON report to a specified URL whenever a violation occurs. This is the best way to test a new policy without breaking your site.

---

## 9. Frontend Integration
In Thymeleaf, you can access the request attribute:
`<script th:inline="javascript" th:attr="nonce=${cspNonce}">`

---

## 10. Strict-Dynamic
A modern CSP feature that tells the browser: "If a script with a valid nonce loads another script, trust that new script too." This simplifies loading complex 3rd party libraries.

---

## 11. Common Mistakes
1. Using a weak random number generator (always use `SecureRandom`).
2. Forgetting to apply the nonce to every inline script.
3. Overly broad policies like `script-src *` (which allows everything).

---

## 12. Quick-Fire Interview Q&A
**Q: Does CSP replace output encoding?**  
A: No. Output encoding is your first line of defense; CSP is your "Defense in Depth" backup.  
**Q: Can I use multiple CSP headers?**  
A: Yes, but the browser will enforce the **strictest** combination of all of them.
