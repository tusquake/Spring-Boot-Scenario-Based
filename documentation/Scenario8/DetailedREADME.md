# JWT Blacklisting & Stateless Logout — Complete Interview Reference

## Table of Contents
1. [The Stateless Nature of JWT](#1-the-stateless-nature)
2. [The Logout Problem in JWT](#2-the-logout-problem)
3. [What is JWT Blacklisting?](#3-what-is-blacklisting)
4. [JTI (JWT ID) vs Token-based Blacklist](#4-jti-vs-token)
5. [The Classic Interview Trap: The Persistence Trade-off](#5-the-classic-interview-trap-persistence)
6. [Implementation with Redis](#6-implementation)
7. [Token Expiration vs Blacklist Expiration](#7-expiration)
8. [Stateless vs Stateful Sessions](#8-stateless-vs-stateful)
9. [Security Considerations (Token Hijacking)](#9-security)
10. [Performance Impact of Blacklist Checks](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. The Stateless Nature of JWT
JWTs are stateless because the server doesn't store session info. The token itself contains all the claims (roles, user ID, expiration) and is verified via a digital signature.

---

## 2. The Logout Problem in JWT
Since the server doesn't maintain a session registry, it cannot "invalidate" a token. Once a JWT is issued, it is valid until it expires, even if the user logs out.

---

## 3. What is JWT Blacklisting?
Blacklisting is a hybrid approach where you maintain a fast-access list of "invalidated" tokens. On every request, you check if the incoming token is in this list.

---

## 4. JTI (JWT ID) vs Token-based Blacklist
Storing the entire JWT in a list is memory-intensive. Using a `jti` (JWT ID) claim—a unique UUID per token—and blacklisting only that ID is much more efficient.

---

## 5. The Classic Interview Trap: The Persistence Trade-off
**The Question**: "If you use a blacklist, aren't you making the app stateful again?"
**The Answer**: Yes, but only for *invalidated* tokens, not all active users. It's much smaller and more scalable than traditional session management.

---

## 6. Implementation with Redis
Redis is the ideal choice for blacklisting due to its sub-millisecond lookups and native support for TTL (Time To Live), which automatically clears expired tokens.

---

## 7. Token Expiration vs Blacklist Expiration
A blacklisted token only needs to stay in the blacklist until its original expiration time. After that, it is invalid anyway and can be safely removed from the list.

---

## 8. Stateless vs Stateful Sessions
- **Stateless**: Scale easily, no DB lookup needed for verification.
- **Stateful**: Easy to revoke, but hard to scale across multiple regions/instances.
- **Hybrid (Blacklist)**: Scalable verification with a targeted revocation mechanism.

---

## 9. Security Considerations (Token Hijacking)
If a token is hijacked, blacklisting it is the only way to stop an attacker before the token naturally expires.

---

## 10. Performance Impact of Blacklist Checks
Performing a Redis check on every single request adds a small latency (1-2ms). This is usually acceptable for the security benefit it provides.

---

## 11. Common Mistakes
1. Storing the whole JWT string in the blacklist (wastes memory).
2. Forgetting to set a TTL in Redis.
3. Checking the blacklist *after* doing expensive database work.

---

## 12. Quick-Fire Interview Q&A
**Q: What is a Refresh Token?**  
A: A long-lived token used to get new Access Tokens without the user logging in again.  
**Q: How do you handle 'Logout from all devices'?**  
A: Use a `version` or `timestamp` in the JWT and DB; increment it in the DB to invalidate all old tokens.
