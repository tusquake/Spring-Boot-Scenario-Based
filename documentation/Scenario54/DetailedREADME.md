# Method Level Security: @PreAuthorize & @PostAuthorize — Complete Interview Reference

## Table of Contents
1. [Introduction to Method Security](#1-introduction)
2. [Enabling Method Security (@EnableMethodSecurity)](#2-enabling)
3. [What is @PreAuthorize?](#3-preauthorize)
4. [What is @PostAuthorize?](#4-postauthorize)
5. [The Classic Interview Trap: Internal Method Calls (Proxies)](#5-the-classic-interview-trap-proxies)
6. [Filtering Results with @PostFilter](#6-postfilter)
7. [Filtering Inputs with @PreFilter](#7-prefilter)
8. [Using SpEL (Spring Expression Language) in Security](#8-spel)
9. [Custom Security Beans for Complex Logic](#9-custom-security-beans)
10. [Performance Impact of Method Security](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to Method Security
While URL-based security (`http.authorizeHttpRequests(...)`) is good for securing endpoints, Method-level security allows you to secure the business logic itself (the Service layer), providing a second layer of defense.

---

## 2. Enabling Method Security
In Spring Security 6.0+, you must add `@EnableMethodSecurity` to a configuration class. This replaces the older `@EnableGlobalMethodSecurity`.

---

## 3. What is @PreAuthorize?
Checks the security expression **BEFORE** the method is executed. If it fails, an `AccessDeniedException` is thrown immediately.
`@PreAuthorize("hasRole('ADMIN')")`

---

## 4. What is @PostAuthorize?
Executes the method first, and then checks the security expression. It has access to the returned object using the `returnObject` variable. Useful for "ownership" checks.
`@PostAuthorize("returnObject.owner == authentication.name")`

---

## 5. The Classic Interview Trap: Proxies
**The Trap**: Method A calls Method B in the same class. Method B has `@PreAuthorize`.
**The Problem**: The security check will **NOT** run. Like AOP and Transactions, Method Security works via Proxies. Internal calls bypass the proxy.

---

## 6. Filtering Results with @PostFilter
Allows you to filter a returned collection or stream based on security rules.
`@PostFilter("filterObject.owner == authentication.name")`

---

## 7. Filtering Inputs with @PreFilter
Allows you to filter a collection passed as a parameter before the method logic runs.

---

## 8. Using SpEL in Security
You can use complex expressions:
- `hasRole('ADMIN')`
- `hasAuthority('SCOPE_read')`
- `isAuthenticated()`
- `isAnonymous()`

---

## 9. Custom Security Beans
For logic that is too complex for a single line of SpEL, you can delegate to a Spring Bean:
`@PreAuthorize("@mySecurityService.canAccess(principal, #id)")`

---

## 10. Performance Impact
Method security adds a small overhead because every method call must be intercepted by a proxy. However, the security benefit usually outweighs the cost.

---

## 11. Common Mistakes
1. Forgetting `@EnableMethodSecurity`.
2. Using `@PreAuthorize` on a `private` method (it won't work).
3. Using `@PostAuthorize` for logic that has side-effects (the side-effect happens even if access is denied).

---

## 12. Quick-Fire Interview Q&A
**Q: When should I use @PostAuthorize over @PreAuthorize?**  
A: Use `@PostAuthorize` when you need to check the data that was actually fetched from the database (e.g., "Is this record's owner the current user?").  
**Q: Can I use @PreAuthorize on a Controller?**  
A: Yes, but it is more common and safer to use it on the Service layer.
