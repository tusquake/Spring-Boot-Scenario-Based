# Clickjacking Protection (X-Frame-Options) — Complete Interview Reference

## Table of Contents
1. [What is Clickjacking?](#1-what-is-clickjacking)
2. [How Clickjacking Attacks Work](#2-how-it-works)
3. [The X-Frame-Options Header](#3-x-frame-options)
4. [X-Frame-Options Directives (DENY vs SAMEORIGIN)](#4-directives)
5. [The Classic Interview Trap: Frame Ancestors (CSP)](#5-the-classic-interview-trap-csp)
6. [Configuring Clickjacking Protection in Spring Security](#6-configuration)
7. [Testing for Clickjacking Vulnerabilities](#7-testing)
8. [Legacy Defense: JavaScript Frame-Busters](#8-frame-busters)
9. [Clickjacking vs UI Redressing](#9-vs-ui-redressing)
10. [When to Allow Framing?](#10-when-to-allow)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Clickjacking?
Clickjacking, also known as "UI redressing", is a malicious technique where an attacker tricks a user into clicking on something different from what the user perceives they are clicking on. This is done by embedding a transparent iframe of the target site over a decoy site.

---

## 2. How Clickjacking Works
1. Attacker creates an attractive page (`evil.com`) with a "Click for Free iPhone" button.
2. Attacker embeds `bank.com/transfer` as a 100% transparent iframe over the button.
3. User clicks "Free iPhone", but actually clicks "Confirm Transfer" on `bank.com`.
4. The action is performed because the user is already logged into their bank.

---

## 3. The X-Frame-Options Header
This is the primary legacy defense against clickjacking. It tells the browser whether it is allowed to render the page inside a `<frame>`, `<iframe>`, or `<object>`.

---

## 4. X-Frame-Options Directives
- **DENY**: The page cannot be displayed in a frame, regardless of the site attempting to do so.
- **SAMEORIGIN**: The page can only be displayed in a frame on the same origin as the page itself.
- **ALLOW-FROM (Deprecated)**: Allows framing only by a specific URI. (Most modern browsers ignore this).

---

## 5. The Classic Interview Trap: CSP frame-ancestors
**The Question**: *"Is X-Frame-Options the most modern way to prevent clickjacking?"*
**The Answer**: **NO**. The modern way is to use the `frame-ancestors` directive in the **Content Security Policy (CSP)**. It is more flexible than `X-Frame-Options` and is the industry standard for modern browsers. If both headers are present, CSP takes precedence.

---

## 6. Configuring in Spring Security
Spring Security enables `X-Frame-Options: DENY` by default. To change it to `SAMEORIGIN` (common for apps that use frames internally):
```java
http.headers(headers -> headers
    .frameOptions(frame -> frame.sameOrigin())
);
```

---

## 7. Testing for Clickjacking
Create a simple HTML page on a different domain and try to embed your sensitive page using `<iframe src="your-url"></iframe>`. If the browser refuses to render it, you are protected.

---

## 8. Legacy Frame-Busters
Before the `X-Frame-Options` header existed, developers used small JavaScript snippets like:
`if (top != self) { top.location = self.location; }`
These are easily bypassed by attackers and should not be relied upon today.

---

## 9. Clickjacking vs UI Redressing
UI Redressing is the broader category of attacks that manipulate the UI. Clickjacking is the specific (and most common) form that uses transparent iframes.

---

## 10. When to Allow Framing?
You might want to allow framing if:
1. You are building a widget intended to be embedded on other sites (e.g., a "Like" button).
2. Your application uses a legacy multi-frame architecture.

---

## 11. Common Mistakes
1. Using `ALLOW-FROM` in 2024 (it has poor browser support).
2. Forgetting that `permitAll()` endpoints also need clickjacking protection.
3. Disabling frame protection globally instead of for specific paths.

---

## 12. Quick-Fire Interview Q&A
**Q: Does Clickjacking affect mobile apps?**  
A: It mainly affects mobile web browsers. Native apps have different UI redressing risks but not classic iframe-based clickjacking.  
**Q: What is the difference between DENY and SAMEORIGIN?**  
A: DENY blocks everyone (even your own site); SAMEORIGIN allows your own site to frame its own pages.
