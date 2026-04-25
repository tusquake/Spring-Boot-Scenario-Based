# Session Management & Fixation — Complete Interview Reference

## Table of Contents
1. [What is Session Management?](#1-what-is-session-management)
2. [Session Creation Strategies (Stateless vs Stateful)](#2-session-strategies)
3. [What is Session Fixation?](#3-session-fixation)
4. [Fixation Protection: migrateSession vs newSession](#4-fixation-protection)
5. [The Classic Interview Trap: Concurrent Session Control](#5-the-classic-interview-trap-concurrent)
6. [Handling Session Timeouts](#6-session-timeouts)
7. [Invalid Session Redirects](#7-invalid-session)
8. [Session Management with Spring Session (Redis/JDBC)](#8-spring-session)
9. [The Role of the JSESSIONID Cookie](#9-jsessionid)
10. [Session Management and CSRF](#10-session-csrf)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Session Management?
Session management is the process of securely handling multiple requests from the same user over time. Since HTTP is stateless, the server uses a "Session ID" (usually in a cookie) to recognize the user.

---

## 2. Session Creation Strategies
- **ALWAYS**: A session will always be created.
- **NEVER**: Spring will never create a session, but will use one if it already exists.
- **IF_REQUIRED**: (Default) Spring creates a session only if needed (e.g., to store security context).
- **STATELESS**: Spring will never create or use a session (Common for JWT APIs).

---

## 3. What is Session Fixation?
Session fixation is an attack where an attacker "fixes" a user's session ID.
1. Attacker gets a valid Session ID from the server.
2. Attacker tricks the user into using that ID (e.g., via a link).
3. User logs in.
4. Attacker can now use the same Session ID to access the user's account.

---

## 4. Fixation Protection
Spring Security prevents this by changing the Session ID immediately after a user logs in.
- `migrateSession`: (Default) Creates a new session and copies all attributes from the old one.
- `newSession`: Creates a new session without copying attributes.

---

## 5. The Classic Interview Trap: Concurrent Sessions
**The Question**: *"How do you prevent a user from logging in from two different browsers at the same time?"*
**The Answer**: Use `maximumSessions(1)`. You can also decide whether to "expire" the old session or "block" the new login attempt.

---

## 6. Session Timeouts
Configured in `application.properties`:
`server.servlet.session.timeout=30m`
After 30 minutes of inactivity, the session is invalidated by the container.

---

## 7. Invalid Session Redirects
You can configure Spring Security to redirect the user to a specific URL (like `/session-expired`) if they try to use an ID that is no longer valid in the server's memory.

---

## 8. Spring Session (Distributed Sessions)
In a clustered environment (multiple servers), standard Tomcat sessions won't work because they are stored in memory. **Spring Session** allows you to store sessions in a shared database like **Redis** or **MySQL**.

---

## 9. The JSESSIONID Cookie
This is the standard cookie name used by Java web servers to track sessions. You should always mark it as `HttpOnly` and `Secure`.

---

## 10. Session Management and CSRF
Session-based applications are vulnerable to CSRF. Stateless (JWT) applications are not (unless the JWT is in a cookie).

---

## 11. Common Mistakes
1. Using `STATELESS` but then trying to use `HttpSession` in your code.
2. Not configuring a `SessionRegistry` bean when using concurrent session control.
3. Forgetting that `migrateSession` is active by default (it can interfere with custom session logic).

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between a Session and a Cookie?**  
A: A cookie is stored on the client (browser); a session is stored on the server.  
**Q: How do you log out a user programmatically?**  
A: `session.invalidate()` or using Spring Security's `SecurityContextLogoutHandler`.
