# Scenario 48: MDC Tracing (Cross-Layer Logs)

Demonstrates how to maintain context (like a UserID or TraceID) across different layers of your application without passing it as a parameter every time.

## Concept
**MDC (Mapped Diagnostic Context)** allows you to "Stick" a value to the current thread. Any log line printed by that thread will automatically include this value.

## Implementation Details
We used a Service layer where we "Push" and "Pop" values from the MDC.

### Code Snippet:
```java
MDC.put("traceId", UUID.randomUUID().toString());
logger.info("Service processing...");
MDC.clear();
```

## Verification Results
- **URL**: `/api/scenario48/trace`
- **Output**: Check console logs.
- **Result**: Every log line from the Controller and the Service will have the same `[traceId=...]` prefix, making it easy to see which logs belong to the same request.

## Interview Theory: Context Management
- **Thread Local**: MDC uses `ThreadLocal` under the hood. 
- **The Danger**: Always use `MDC.clear()` in a `finally` block! Otherwise, if a thread is returned to the pool, the next user might "inherit" the previous user's trace ID.
- **Async Pitfall**: Mention that MDC context is LOST when you switch threads (e.g., using `@Async`). You must use a "Task Decorator" to copy the context to the new thread.
