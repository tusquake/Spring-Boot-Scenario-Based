# Session Fixation & Concurrent Sessions — Complete Interview Reference

## Table of Contents
1. [What is a Session?](#1-introduction)
2. [Session Fixation Attack Explained](#2-session-fixation)
3. [Fixing Session Fixation (migrateSession)](#3-fixing-fixation)
4. [Concurrent Session Control](#4-concurrent-sessions)
5. [How Spring Tracks Sessions](#5-session-registry)
6. [The Classic Interview Trap: Max Sessions prevents Login](#6-the-classic-interview-trap-trap)
7. [Session Creation Policies](#7-creation-policies)
8. [Session Timeouts vs Cookie Expiry](#8-timeouts)
9. [Clustered Sessions (Spring Session)](#9-clustered-sessions)
10. [HttpSessionEventPublisher: Why it's required](#10-event-publisher)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Session?
A session is a server-side data structure that stores information about a specific user across multiple requests. It is identified by a `JSESSIONID` cookie sent by the browser.

---

## 2. Session Fixation Attack
**The Attack**: An attacker visits your site, gets a `JSESSIONID` (e.g., "123"), and then tricks a victim into using that same ID (e.g., via a link like `site.com;jsessionid=123`). The victim logs in. Now the attacker, who already knows the ID "123", is also logged in as the victim.

---

## 3. Fixing Fixation
Spring Security protects against this by default using **Session Fixation Protection**.
- **migrateSession()**: (Default) Creates a new session and copies all attributes from the old session to the new one.
- **newSession()**: Creates a new session without copying attributes.
- **none()**: No protection (Dangerous).

---

## 4. Concurrent Sessions
Spring allows you to limit the number of active sessions a single user can have.
- **maxSessions(1)**: A user can only be logged in from one device/browser at a time.

---

## 5. Session Registry
Spring uses a `SessionRegistry` to keep track of all active sessions. This allows the server to look up which sessions belong to which username and expire them remotely.

---

## 6. The Classic Interview Trap: Preventing Login
**The Trap**: You set `maximumSessions(1)`.
**The Question**: If the user is logged in on Chrome and tries to log in on Firefox, what happens?
**The Answer**: It depends on `maxSessionsPreventsLogin`.
- **False (Default)**: The Firefox login succeeds, and the Chrome session is **expired**.
- **True**: The Firefox login **fails** with an error. The user must manually log out from Chrome before they can log in anywhere else.

---

## 7. Session Creation Policies
- **ALWAYS**: A session is always created if it doesn't exist.
- **NEVER**: Spring Security will never create a session, but it will use one if it already exists.
- **IF_REQUIRED**: (Default) Create a session only if required (e.g., for CSRF or Auth).
- **STATELESS**: No session will be created or used.

---

## 8. Session Timeout
Defined in `application.properties`: `server.servlet.session.timeout=30m`.
If the user doesn't make a request for 30 minutes, the session is purged from the server's memory.

---

## 9. Clustered Sessions
Standard sessions are stored in the JVM memory. If you have multiple server nodes, a user logged in on Node A will be "logged out" if their next request hits Node B.
**Solution**: Use **Spring Session Redis** or **Spring Session JDBC** to store sessions in a central database shared by all nodes.

---

## 10. HttpSessionEventPublisher
This bean is **MANDATORY** for concurrent session management. It listens for HTTP session destruction events and notifies Spring Security to update the `SessionRegistry`. Without it, Spring will never know when a session has timed out, and the user's session count will never decrease.

---

## 11. Common Mistakes
1. Forgetting the `HttpSessionEventPublisher` bean.
2. Using `STATELESS` for a traditional web app with forms.
3. Not realizing that `migrateSession()` only works if the session was created **before** login.

---

## 12. Quick-Fire Interview Q&A
**Q: Is JSESSIONID secure?**  
A: Yes, if the `HttpOnly` and `Secure` flags are set on the cookie.  
**Q: What is the benefit of session fixation protection?**  
A: It ensures that an attacker cannot "pre-assign" a session ID to a victim before they log in.
