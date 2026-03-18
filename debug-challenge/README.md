# Spring Boot Security Interview Preparation Guide

This repository contains a specialized "Debug Challenge" application modified to demonstrate core and advanced Spring Boot Security concepts. Each scenario covers a critical area often discussed in technical interviews.

## Table of Contents
1. [Scenario 56: Securing Actuator Endpoints](#scenario-56-securing-actuator-endpoints)
2. [Scenario 57: Content Security Policy (CSP)](#scenario-57-content-security-policy-csp)
3. [Scenario 58: Password Encoding & URL Order](#scenario-58-password-encoding--url-order)
4. [Scenario 59: Roles vs Authorities](#scenario-59-roles-vs-authorities)
5. [Scenario 60: Role Hierarchy](#scenario-60-role-hierarchy)
6. [Scenario 61: Custom Exception Handling (401/403)](#scenario-61-custom-exception-handling-401403)
7. [Scenario 62: Ignoring vs Permitting All](#scenario-62-ignoring-vs-permitting-all)
8. [Scenario 63: Session Management](#scenario-63-session-management)
9. [Scenario 64: Command Injection Prevention](#scenario-64-command-injection-prevention)

---

### Scenario 56: Securing Actuator Endpoints
- **Concept**: Protecting sensitive management data.
- **Implementation**: Access to `/actuator/**` is restricted to `ROLE_ADMIN`.
- **Key Takeaway**: External monitoring tools can expose your environment variables; never leave actuators unprotected in production.

### Scenario 57: Content Security Policy (CSP)
- **Concept**: XSS Prevention using browser-level restrictions.
- **Implementation**: A custom `CspNonceFilter` generates a secure random nonce for every request and injects it into the `Content-Security-Policy` header.
- **Interview Tip**: Explain how a Nonce is better than just 'self' because it stops inline script attacks.

### Scenario 58: Password Encoding & URL Order
- **Concept**: Future-proofing security and understanding filter precedence.
- **Implementation**: used `DelegatingPasswordEncoder` (supporting BCrypt, Argon2, etc.). Configured matcher order to ensure specific rules take precedence over general ones.
- **Key Takeaway**: Security rules are first-match-wins. Specific paths must come BEFORE generic `/**` paths.

### Scenario 59: Roles vs Authorities
- **Concept**: The `ROLE_` prefix convention.
- **Implementation**: Showed that `hasRole("ADMIN")` looks for `ROLE_ADMIN`, whereas `hasAuthority("ADMIN")` looks for a literal match.
- **Implementation**: Added a `JwtAuthenticationConverter` to normalize strings like "ADMIN" into "ROLE_ADMIN".

### Scenario 60: Role Hierarchy
- **Concept**: Simplifying permissions through inheritance.
- **Implementation**: Configured `ROLE_ADMIN > ROLE_USER`. This ensures an Admin user automatically has all access granted to a regular User.

### Scenario 61: Custom Exception Handling (401/403)
- **Concept**: Controlling API error responses outside the Controller.
- **Implementation**:
    - **`AuthenticationEntryPoint`**: Handles **401 Unauthorized** (invalid/missing token).
    - **`AccessDeniedHandler`**: Handles **403 Forbidden** (authenticated but lacks role).
- **Difference**: `@ControllerAdvice` usually can't catch these because they happen in the Filter Chain before reaching the Controller.

### Scenario 62: Ignoring vs Permitting All
- **Concept**: Performance vs Security (The Filter Bypass).
- **Implementation**: 
    - `web.ignoring()`: Bypasses the chain. NO security headers, NO CSRF. Best for static assets (CSS/Images).
    - `http.permitAll()`: Stays in the chain. Includes security headers and protection. Best for public APIs (Login/Register).

### Scenario 63: Session Management
- **Concept**: Stateless tracking and Fixation protection.
- **Implementation**:
    - **Fixation**: Using `migrateSession()` to change IDs upon login.
    - **Concurrency**: `maximumSessions(1)` to prevent multiple logins (The Netflix Model).
    - **Policy**: `IF_REQUIRED` vs `STATELESS`.

### Scenario 64: Command Injection Prevention
- **Concept**: Securely interacting with the OS.
- **Implementation**: Replaced unsafe String concatenation in `Runtime.exec()` with **Strict Regex Validation** and **ProcessBuilder** (passing arguments as a list).
- **Key Takeaway**: ProcessBuilder prevents the shell from interpreting characters like `&&` or `;` as separate commands.

---

## How to Run & Verify
- Start the app: `mvn spring-boot:run`
- Use individual scenario endpoints in `/api/scenarioXX/...` to test behaviors.
- Review `SecurityConfig.java` for centralized configuration.
