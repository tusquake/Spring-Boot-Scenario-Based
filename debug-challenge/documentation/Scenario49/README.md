# Scenario 49: Asynchronous Processing (@Async)

Demonstrates how to perform non-blocking background tasks to keep your API responsive.

## Concept
Tasks like "Sending a Welcome Email" or "Generating a PDF" take several seconds. The user shouldn't have to wait for these. `@Async` allows the controller to return a "Accepted" response immediately while the task runs in the background.

## Implementation Details
1. Enabled via `@EnableAsync`.
2. Decorated service methods with `@Async`.

### Service Snippet:
```java
@Async
public void runLongTask(String name, int seconds) {
    Thread.sleep(seconds * 1000);
    logger.info("Task {} complete!", name);
}
```

## Verification Results
- **URL**: `/api/scenario49/run?name=ReportGen`
- **Observation**: The API responds in **~10ms**.
- **Result**: Check the logs 10 seconds later; you will see the "Task ReportGen complete!" message appearing on a separate `task-executor` thread.

## Interview Theory: Async Best Practices
- **Rules**:
  1. The method must be `public`.
  2. You cannot call the `@Async` method from within the same class (Self-invocation bypasses the proxy).
- **Return Types**: Use `void` for "Fire and Forget" or `CompletableFuture<T>` if you eventually need the result back.
- **Executors**: Mention that you should always define a custom `ThreadPoolTaskExecutor` to prevent Spring from using the default unbounded executor, which could crash the server under heavy load.
