# OAuth2 Scopes vs Roles — Complete Interview Reference

## Table of Contents
1. [What are OAuth2 Scopes?](#1-what-are-scopes)
2. [OAuth2 Roles (Authorities)](#2-roles)
3. [Scopes vs Roles: Key Differences](#3-scopes-vs-roles)
4. [The OAuth2 Flow (Resource Server perspective)](#4-oauth2-flow)
5. [The Classic Interview Trap: SCOPE_ Prefix](#5-the-classic-interview-trap-scope-prefix)
6. [Configuring @PreAuthorize for Scopes](#6-preauthorize)
7. [The JWT Decoder and Claims](#7-jwt-claims)
8. [Multi-tenant Resource Servers](#8-multi-tenant)
9. [Token Introspection vs JWT Verification](#9-introspection-vs-jwt)
10. [Handling Insufficient Scope (403 vs 401)](#10-insufficient-scope)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are OAuth2 Scopes?
Scopes are a mechanism in OAuth 2.0 to limit an application's access to a user's account. An application can request one or more scopes (e.g., `read`, `write`, `profile`), and this is then displayed to the user on the consent screen.

---

## 2. OAuth2 Roles (Authorities)
Roles (e.g., `ROLE_ADMIN`, `ROLE_USER`) represent the user's permissions within the system, regardless of which application they are using.

---

## 3. Scopes vs Roles
- **Scopes**: What the **Application** is allowed to do on behalf of the user.
- **Roles**: What the **User** is allowed to do in the system.

---

## 4. The OAuth2 Flow
In a Resource Server, the incoming request contains a JWT. The Resource Server verifies the signature of the JWT and extracts the `scp` or `scope` claim to determine if the application has the necessary permissions.

---

## 5. The Classic Interview Trap: SCOPE_ Prefix
**The Trap**: Your JWT has a claim `"scope": "read"`. You use `@PreAuthorize("hasAuthority('read')")`, but it fails.
**The Fix**: Spring Security's `JwtAuthenticationConverter` automatically adds the `SCOPE_` prefix to all authorities extracted from the scope claim. You must use `@PreAuthorize("hasAuthority('SCOPE_read')")`.

---

## 6. Configuring @PreAuthorize
You can use `hasAuthority('SCOPE_xyz')` to check for specific scopes. This is much more granular than checking for a general `ROLE_USER`.

---

## 7. JWT Claims
A standard OAuth2 JWT contains:
- `sub`: Subject (User ID).
- `aud`: Audience (Client ID).
- `scope`: The list of granted scopes.

---

## 8. Multi-tenant Resource Servers
A Resource Server can be configured to accept tokens from multiple different Authorization Servers (Issuers) by using a dynamic `AuthenticationManager`.

---

## 9. Token Introspection vs JWT Verification
- **JWT Verification**: The Resource Server verifies the token locally using a public key. (Faster, stateless).
- **Introspection**: The Resource Server calls the Authorization Server to check if the token is still valid. (Slower, but supports instant revocation).

---

## 10. Handling Insufficient Scope
- **401 Unauthorized**: No valid token provided.
- **403 Forbidden**: Token is valid, but doesn't have the required scope.

---

## 11. Common Mistakes
1. Mixing up Scopes and Roles in business logic.
2. Forgetting the `SCOPE_` prefix in Spring Security expressions.
3. Not verifying the `aud` (Audience) claim, which could allow a token intended for another service to be used in yours.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a token have multiple scopes?**  
A: Yes, scopes are usually space-separated in the JWT.  
**Q: What is a Resource Server?**  
A: The server hosting the protected data (your Spring Boot API).
