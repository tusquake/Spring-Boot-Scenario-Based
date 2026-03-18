# Scenario 54: Advanced Method Security

Demonstrates granular, logic-based security that goes beyond simple "Is Admin?" checks.

## Concept
1. **Pre-Authorization**: Check permissions *before* running the method (e.g., "Are you the owner of this account?").
2. **Post-Authorization**: Check permissions *after* finding the data (e.g., "Now that I found the account, are you allowed to see its balance?").
3. **Expression Language (SpEL)**: Using complex logic inside the annotations.

## Implementation Details
We used `@PreAuthorize` with custom bean logic and `@PostAuthorize` to compare the returned result with the logged-in user.

### SpEL Snippet:
```java
@PreAuthorize("@bankSecurity.isOwner(#id)")
public Double withdraw(...) { ... }
```

## Verification Results
- **Scenario**: User Alice tries to view User Bob's account.
- **Result**: The code finds Bob's account, but `@PostAuthorize` sees that Alice's name doesn't match the account owner and throws a `403 Access Denied`.

## Interview Theory: Security Best Practices
- **Service Layer Security**: Always put security on the Service layer, not just the Controller. This ensures that even internal calls (e.g., from a scheduled job or another service) are still protected.
- **Global Method Security**: Mention you must enable this via `@EnableMethodSecurity`.
- **Performance**: While powerful, complex SpEL expressions can be slow if they involve database hits. Use them wisely!
