# Scenario 8: JWT Authentication & Token Blacklisting

Demonstrates a full JWT login flow with a secure Logout mechanism using a JTI-based blacklist.

## Concept
JWTs are "stateless," meaning once issued, the server can't easily "revoke" them. If a user logs out, their token is still technically valid until it expires.

## Implementation Details
- **Login**: Generates a JWT with a unique `jti` (JWT ID) claim.
- **Logout**: Extracts the `jti` from the token and stores it in a `TokenBlacklistService`.
- **Verification**: On every request, the security filter checks if the token's `jti` is in the blacklist.

### Logout Logic:
```java
@PostMapping("/logout")
public String logout(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.substring(7);
    String jti = jwtUtils.extractJti(token);
    blacklistService.blacklistToken(jti);
    return "Logout Success!";
}
```

## Verification Results
1. **Login**: Access `/api/scenario8/login?username=Tushar`. Copy the Token.
2. **Access**: Call `/api/scenario8/protected` using the token. -> **Works**.
3. **Logout**: Call `/api/scenario8/logout` with the token.
4. **Access again**: Call `/api/scenario8/protected` again. -> **Fails with 401**, proving the token is now useless.

## Interview Theory: Stateless Logout
- **The Revocation Problem**: Since JWTs aren't stored on the server, you can't "delete" them. You must store a list of "invalid" tokens instead.
- **Storage**: In production, use **Redis** for your blacklist. Set the TTL of the blacklisted JTI to match the remaining life of the token so the list doesn't grow forever.
- **Stateless vs Stateful**: JWTs are great for scale, but the moment you add a blacklist, you are adding a tiny amount of "state" back to your system.
