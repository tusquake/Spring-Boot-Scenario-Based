# Scenario 59: Roles vs Authorities

Understanding the subtle but critical difference between `hasRole()` and `hasAuthority()`.

## Concept
- **Authority**: A granular permission or "right" (e.g., `user:read`, `product:delete`).
- **Role**: A high-level grouping of permissions (e.g., `ADMIN`, `MANAGER`). By convention, Spring Security expects roles to be prefixed with `ROLE_`.

## Implementation Details
We demonstrated how Spring Security handles these internally:
- `hasRole("ADMIN")` looks for an authority named `ROLE_ADMIN`.
- `hasAuthority("ROLE_ADMIN")` looks for the exact same string.
- We implemented a `JwtAuthenticationConverter` to pull scopes from a JWT (e.g., "ADMIN") and automatically prefix them with `ROLE_` so `hasRole()` works seamlessly.

### Normalization Logic:
```java
var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
authoritiesConverter.setAuthorityPrefix("ROLE_"); // Automatically adds the prefix
authoritiesConverter.setAuthoritiesClaimName("scope");
```

## Verification Results
- **Role Test**: `/api/scenario59/role-test` 
  - Result: Accessible only if user has an authority named `ROLE_ADMIN`.
- **Authority Test**: `/api/scenario59/authority-test`
  - Result: Checks for the literal string.

## Interview Theory: The ROLE_ Convention
- **The Trap**: If your Database or JWT has "ADMIN" but your code uses `hasRole("ADMIN")`, the check will **FAIL** because Spring is looking for `ROLE_ADMIN`.
- **Advice**: Always tell the interviewer that you prefer `hasRole()` for high-level UI/Access control and `hasAuthority()` for fine-grained permission checks.
- **Conversion**: Mention that in a stateless JWT environment, the `JwtAuthenticationConverter` is where this "magic" prefixing should happen.
