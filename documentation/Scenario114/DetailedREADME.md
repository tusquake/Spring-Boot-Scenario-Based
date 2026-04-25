# Remember-Me Authentication — Complete Interview Reference

## Table of Contents
1. [What is Remember-Me?](#1-what-is-remember-me)
2. [Cookie-Based Token Approach (Hash-based)](#2-hash-based)
3. [Persistent Token Approach (Database-based)](#3-persistent-token)
4. [Detecting Remember-Me vs Full Login](#4-detecting)
5. [The Classic Interview Trap: Remember-Me Security Risks](#5-the-classic-interview-trap-risks)
6. [Configuring Remember-Me in SecurityFilterChain](#6-configuration)
7. [Customizing Cookie Name and Expiry](#7-customizing)
8. [Remember-Me and Logout](#8-logout)
9. [The Role of UserDetailsService](#9-user-details-service)
10. [Invalidating Tokens on Password Change](#10-invalidation)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Remember-Me?
Remember-me authentication refers to the ability of a website to remember the identity of a user between sessions. This is typically accomplished by sending a cookie to the browser, which is then used to automatically log the user in on subsequent visits.

---

## 2. Hash-Based Approach
- **How it works**: A cookie is created containing the username, expiration time, and a MD5 hash of these values plus the password and a private "key".
- **Pros**: Easy to implement, no database needed.
- **Cons**: If the cookie is stolen, the attacker can log in as the user. If the user changes their password, the cookie becomes invalid.

---

## 3. Persistent Token Approach
- **How it works**: A unique series ID and a random token are stored in a database table (`persistent_logins`). When the user visits, the token is checked and then **regenerated**.
- **Pros**: More secure. If a cookie is stolen and used, the original user will get an error on their next visit, and all tokens for that user can be invalidated.

---

## 4. Detecting the Login Type
In Spring Security, you can check if a user is "fully authenticated" or just "remember-me authenticated".
- **RememberMeAuthenticationToken**: The user was logged in via a cookie.
- **UsernamePasswordAuthenticationToken**: The user provided their password in the current session.

---

## 5. The Classic Interview Trap: isFullyAuthenticated()
**The Trap**: A user wants to "Delete Account" or "Change Password".
**The Problem**: If the user is logged in via Remember-Me, it might be an attacker who found a logged-in laptop.
**The Fix**: Use `isFullyAuthenticated()` in your security expressions. This forces the user to provide their password again for sensitive operations, even if they were logged in automatically.
`@PreAuthorize("isFullyAuthenticated()")`

---

## 6. Configuration
```java
http.rememberMe(remember -> remember
    .key("uniqueAndSecret")
    .tokenValiditySeconds(86400) // 1 day
);
```

---

## 7. UserDetailsService
Remember-Me requires a `UserDetailsService` to reload the user's authorities and details from the database using the username stored in the token.

---

## 8. Logout
When a user explicitly logs out, Spring Security automatically deletes the remember-me cookie and, if using the persistent approach, removes the token from the database.

---

## 9. PersistentTokenRepository
To use the database approach, you must provide an implementation of `PersistentTokenRepository` (usually `JdbcTokenRepositoryImpl`) and create the necessary `persistent_logins` table.

---

## 10. Security Key
The `key` used in the hash is critical. If you don't provide one, Spring Boot generates a random one at startup. This means if you restart your server, all existing remember-me cookies will become invalid!

---

## 11. Common Mistakes
1. Not providing a fixed `key` (causing logout on restart).
2. Not using `isFullyAuthenticated()` for sensitive endpoints.
3. Forgetting to include the `remember-me` checkbox in the login form (it must be named exactly `remember-me`).

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use Remember-Me with JWT?**  
A: JWT is naturally "remember-me" if the expiration is long. However, Spring's Remember-Me feature is specifically designed for standard Session-based apps.  
**Q: What is the default validity of a Remember-Me cookie?**  
A: Two weeks (1,209,600 seconds).
