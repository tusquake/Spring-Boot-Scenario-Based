# Scenario 63: Session Management

Managing user state, preventing session hijacking, and controlling concurrent logins.

## Concept
- **Session Fixation**: An attack where a hacker "pre-sets" a session ID for a victim.
- **Concurrent Control**: Controlling how many devices can use an account simultaneously.
- **Session Policy**: Deciding how strictly the server creates or uses session state.

## Implementation Details
In `SecurityConfig.java`, we configured the logic that mirrors a service like **Netflix** or **Hotstar**.

### Configuration:
- **Fixation**: `migrateSession()` generates a fresh ID as soon as a user logs in.
- **Concurrency**: `maximumSessions(1)` restricts the user to one device.
- **Infrastructure**: Added `HttpSessionEventPublisher` to ensure Spring can track session lifespan.

## Verification Results
- **Endpoint**: `/api/scenario63/session-info`.
- **Observation**: Server issues a `Set-Cookie: JSESSIONID=...`.
- **Log Activity**: When logging in, the old session is discarded and a new one is issued (Fixation protection).

## Interview Theory: Real-World Usage
- **Stateless APIs (JWT)**: For modern APIs, we usually use `SessionCreationPolicy.STATELESS`. This tells Spring NOT to create a `JSESSIONID`, saving memory and allowing the app to scale horizontally across multiple servers.
- **Session Fixation**: Always explain that `migrateSession()` is the gold standard because it destroys the "pre-auth" session and gives the authenticated user a clean, private one.
- **Concurrent Limit**: Mention the `HttpSessionEventPublisher` bean. Without it, Spring won't know when a user closes their browser, which can accidentally lock them out of their own account.
