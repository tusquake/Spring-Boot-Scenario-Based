# Scenario 56: Securing Actuator Endpoints

Protecting sensitive management data and operational endpoints from unauthorized access.

## Concept
Spring Boot Actuator provides several endpoints (like `/health`, `/info`, `/env`, `/beans`) to monitor and manage your application. While `/health` is often public, endpoints like `/env` can leak database credentials or environment secrets.

## Implementation Details
In `SecurityConfig.java`, we restricted access to all actuator endpoints except `/health`.

### Configuration:
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/scenario8/login").permitAll()
    // ...
    .requestMatchers("/actuator/health").permitAll()
    .requestMatchers("/actuator/**").hasAuthority("ROLE_ADMIN")
    // ...
)
```

## Verification Results
1. **Public Access**: 
   - **URL**: `http://localhost:8080/actuator/health`
   - **Result**: `{"status":"UP"}` (Works without login).
2. **Restricted Access**: 
   - **URL**: `http://localhost:8080/actuator/env`
   - **Result**: `401 Unauthorized` or `403 Forbidden` (Depending on login state).
   - **Observation**: The server correctly blocks access to sensitive environment data for non-admin users.

## Interview Theory: Why secure Actuator?
- **Sensitive Data Exposure**: Endpoints like `/heapdump`, `/beans`, or `/env` can expose sensitive data (Passwords, API Keys, system architecture) or allow attackers to shut down the app.
- **Production Best Practice**: Always expose only the minimum necessary (`/health`) to the public. Use `management.endpoints.web.exposure.include` in `application.properties` to specifically pick which ones are even available.
- **Role vs Authority**: In this scenario, we use `hasAuthority("ROLE_ADMIN")`. This checks for the exact string match including the `ROLE_` prefix, showing that you understand how Spring Security maps permissions internally.
