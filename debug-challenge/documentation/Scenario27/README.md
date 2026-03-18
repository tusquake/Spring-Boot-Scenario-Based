# Scenario 27: Custom AOP (Aspect Oriented Programming)

Demonstrates how to create your own "Interceptors" for common cross-cutting concerns like logging or timing.

## Concept
AOP allows you to add behavior (like timing a method) to existing code without actually touching that code. This keeps your business logic "Clean."

## Implementation Details
We created a custom annotation `@TrackTime` and an Aspect class that uses `ProceedingJoinPoint`.

### usage:
```java
@TrackTime("slowTask")
@GetMapping("/slow-task")
public Map<String, Object> slowTask() { ... }
```

### Aspect Logic:
```java
@Around("@annotation(trackTime)")
public Object logTime(ProceedingJoinPoint joinPoint, TrackTime trackTime) {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed(); // Calls the actual controller method
    long timeDiff = System.currentTimeMillis() - start;
    logger.info("Timer: {} executed in {}ms", trackTime.value(), timeDiff);
    return result;
}
```

## Verification Results
- **URL**: `/api/scenario27/slow-task`
- **Check Logs**: You will see an INFO line: `Timer: slowTask executed in 1245ms`.
- **Result**: The timing was captured automatically just by adding the annotation.

## Interview Theory: AOP Terminology
- **Aspect**: The class containing the logic (the "Timer" logic).
- **Advice**: The code that runs (the `@Around` method).
- **Pointcut**: Expressions that define *where* the advice should apply (e.g., "on all methods with @TrackTime").
- **Join Point**: A specific point in the execution (the actual method call).
