# Scenario 53: OAuth2 Resource Server

Demonstrates "Modern" security where the backend doesn't handle login, but instead validates JWT tokens from an Identity Provider (like Keycloak, Okta, or Google).

## Concept
The backend acts as a **Resource Server**. It receives a Bearer Token (JWT), validates its signature, and checks the **Scopes** (Permissions) inside the token.

## Implementation Details
Used `@PreAuthorize("hasAuthority('SCOPE_read')")` to restrict access based on token contents.

### Code Snippet:
```java
@GetMapping("/read")
@PreAuthorize("hasAuthority('SCOPE_read')")
public Map<String, String> readData() { ... }
```

## Verification Results
- **No Token**: `/api/scenario53/read` -> Returns `401 Unauthorized`.
- **Token with 'read' Scope**: Returns data successfully.
- **Token with 'write' Scope trying to 'read'**: Returns `403 Forbidden`.

## Interview Theory: OAuth2 & Scopes
- **Stateless**: The server doesn't keep a session. Every request must carry the token. This makes scaling horizontally (multiple instances) very easy.
- **Roles vs Scopes**: 
  - **Scopes** represent *what* the application is allowed to do (e.g., "read", "write").
  - **Roles** represent *who* the user is (e.g., "Admin", "Manager").
- **JWT**: Mention that the server validates the JWT using the Identity Provider's "Public Key" (JWKS).
