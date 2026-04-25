# JWT & Secure Cookie Management — Complete Interview Reference

## Table of Contents
1. [What is a JWT (JSON Web Token)?](#1-what-is-jwt)
2. [Structure of a JWT (Header, Payload, Signature)](#2-structure)
3. [JWT vs Session-Based Auth](#3-jwt-vs-session)
4. [Storing JWT: LocalStorage vs HttpOnly Cookies](#4-storage)
5. [SameSite Cookie Attribute (Lax, Strict, None)](#5-samesite)
6. [The Classic Interview Trap: CSRF in Cookie-based JWT](#6-the-classic-interview-trap-csrf)
7. [HttpOnly and Secure Flags](#7-flags)
8. [Token Expiration and Refresh Strategy](#8-refresh)
9. [Revoking JWTs (The Stateless Problem)](#9-revocation)
10. [Validating JWTs in Spring Boot](#10-validation)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a JWT?
A JSON Web Token is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object.

---

## 2. Structure
- **Header**: Contains the algorithm (HS256) and token type.
- **Payload**: Contains the "claims" (User ID, Roles, Expiration).
- **Signature**: Ensures the token hasn't been tampered with. It is created by signing the base64-encoded header and payload with a secret key.

---

## 3. JWT vs Session
- **Session**: Stateful. The server stores user data in memory/DB. Requires a `SessionID` cookie.
- **JWT**: Stateless. All user data is inside the token. The server doesn't need to store anything. Excellent for scaling.

---

## 4. Storage: LocalStorage vs Cookie
- **LocalStorage**: Easy to use with JS. **Vulnerable to XSS** (any script on your page can read the token).
- **HttpOnly Cookie**: The browser handles it. **Invisible to JS**. Immune to XSS but vulnerable to CSRF.

---

## 5. SameSite Attribute
This attribute tells the browser whether to send the cookie on cross-site requests.
- **Strict**: Cookie sent only for same-site requests.
- **Lax**: (Default) Cookie sent for same-site and top-level GET navigations.
- **None**: Cookie sent for everything (Requires `Secure=true`).

---

## 6. The Classic Interview Trap: CSRF
**The Trap**: A user says, *"I switched from LocalStorage to HttpOnly Cookies for security. Am I safe?"*
**The Answer**: You traded one risk for another. You are now safe from **XSS** (token theft), but you are now vulnerable to **CSRF** (Cross-Site Request Forgery).
**The Fix**: You must implement a CSRF protection mechanism (like a custom header or a double-submit cookie) when using cookies for auth.

---

## 7. Security Flags
- **HttpOnly**: Prevents `document.cookie` access in JS.
- **Secure**: Ensures the cookie is only sent over HTTPS.
- **MaxAge**: Defines when the cookie expires.

---

## 8. Refresh Strategy
JWTs should be short-lived (e.g., 15 minutes). To avoid logging the user out, use a **Refresh Token** (long-lived) to exchange for a new **Access Token** (short-lived).

---

## 9. Revocation
Since JWTs are stateless, you cannot "log out" a user in the traditional sense.
**Solutions**:
1. Keep a "Blacklist" of revoked tokens in Redis.
2. Change the secret key (logs everyone out).
3. Use a "Token Version" in the DB and check it on every request.

---

## 10. Validation in Spring
Use a library like `jjwt` or Spring Security's native OAuth2 support to parse the token, verify the signature, and check the `exp` claim.

---

## 11. Common Mistakes
1. Using a weak secret key (HS256 requires at least 256 bits).
2. Putting sensitive data (passwords, PII) in the payload (remember: the payload is just base64-encoded, anyone can read it).
3. Not checking the `alg` header (vulnerability where an attacker can switch `HS256` to `None`).

---

## 12. Quick-Fire Interview Q&A
**Q: Can I read a JWT without the secret key?**  
A: Yes, you can read the data. You only need the secret key to **verify** if the data is authentic.  
**Q: What is the 'sub' claim?**  
A: It stands for 'Subject', and it usually stores the User ID or Username.
