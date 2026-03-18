# Scenario 58: Configurable Password Encoding & URL Order

Demonstrates future-proof password security using DelegatingPasswordEncoder and the "First Match Wins" rule of Spring Security.

## Concept
1. **DelegatingPasswordEncoder**: A mechanism that allows multiple password encoding algorithms (BCrypt, Argon2, PBKDF2) to coexist. This is vital for migrating legacy systems to modern standards without forcing a bulk password reset.
2. **URL Order**: Spring Security evaluates URL matchers in a top-down approach. The first rule that matches the URL is applied, and the rest are ignored.

## Implementation Details
- **Dynamic Encoder**: We replaced the static `BCryptPasswordEncoder` with `PasswordEncoderFactories.createDelegatingPasswordEncoder()`.
- **Precedence**: Configured the `SecurityFilterChain` so that specific paths (like `/api/scenario8/login`) are defined **before** generic paths (like `/**`).

### SecurityConfig Snippet:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

## Verification Results
- **Password Storage**: If a user is saved with `{bcrypt}$2a$...`, the delegating encoder automatically uses BCrypt. If saved with `{argon2}$argon2id$...`, it uses Argon2.
- **Order Check**: Accessing a sub-path shows it follows the specific restriction (e.g., permitAll) instead of the global authenticated rule.

## Interview Theory: Password Security
- **The Prefix Strategy**: Delegating encoders store passwords with an ID prefix: `{id}hash`. Common IDs include `bcrypt`, `argon2`, `pbkdf2`, and even `noop` (for plain text, though NEVER use in production).
- **Security Chain Order**: A common mistake is putting `.requestMatchers("/**").authenticated()` at the top. This would break your login page because the rule for `/**` would "swallow" the login page request and demand authentication before you can even log in!
- **Rule of Thumb**: Always go from **Most Specific** to **Most General**.
