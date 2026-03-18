# Scenario 52: CORS Deep Dive

Demonstrates how to securely allow a Frontend (e.g., React on port 3000) to talk to your Backend (on port 8080).

## Concept
Browsers have a **Same-Origin Policy** that blocks cross-site requests. **CORS (Cross-Origin Resource Sharing)** is the mechanism to safely "Puncture a hole" in that policy for trusted domains.

## Implementation Details
Configured via `WebMvcConfigurer` to allow specific origins, methods (GET/POST), and headers.

### Configuration Snippet:
```java
registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:3000")
    .allowedMethods("GET", "POST");
```

## Verification Results
- **Blocked**: Hit the API from a different domain (e.g., a local HTML file). Browser console shows "CORS Error".
- **Allowed**: Hit from `localhost:3000`. The browser sends a "Pre-flight" OPTIONS request, the server says OK, and the actual request succeeds.

## Interview Theory: CORS Security
- **Pre-flight**: Explain that for "unsafe" methods (POST/PUT/DELETE), the browser sends an `OPTIONS` request first to check permissions.
- **Wildcards**: NEVER use `allowedOrigins("*")` in production. It defeats the purpose of CORS.
- **Spring Security**: Remind the interviewer that if you use Spring Security, you must configure CORS in *both* the MVC layer and the Security filter chain.
