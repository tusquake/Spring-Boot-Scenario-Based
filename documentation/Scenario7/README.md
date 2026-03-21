# Scenario 7: Async Processing & Security Context

Demonstrates how to run tasks in the background using `@Async` while preserving the user's security context.

## Concept
By default, `@Async` methods run on a different thread. This is a problem for Spring Security because the `SecurityContext` is stored in a `ThreadLocal`, which doesn't cross thread boundaries.

## Implementation Details
We use a `CompletableFuture` to return a result from a background task and ensure the user's details are still accessible.

### Async Service Snippet:
```java
@Async
public CompletableFuture<String> processAsyncTask() {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    return CompletableFuture.completedFuture("Task completed by: " + currentUser);
}
```

## Verification Results
- **URL**: `http://localhost:8080/api/scenario7/test`
- **Result**: `"Task completed by: Tushar_Seth"`
- **Observation**: Even though the task ran on a "task-executor" thread, it still knew the username from the main thread's security context.

## Interview Theory: Async Security
- **The Problem**: `ThreadLocal` storage.
- **The Solution**: Use the `DelegatingSecurityContextAsyncTaskExecutor` or set the strategy to `MODE_INHERITABLETHREADLOCAL`.
- **Performance**: `@Async` is great for "Fire and Forget" tasks like sending emails or log processing, but it requires a properly configured `ThreadPoolTaskExecutor` to avoid running out of threads.
