# Scenario 27: Aspect-Oriented Programming (AOP)

A comprehensive guide to separating cross-cutting concerns from business logic using Spring AOP.

## What is AOP?
**Aspect-Oriented Programming (AOP)** is a programming paradigm that allows you to separate **cross-cutting concerns** from business logic. It focuses on "aspects" like logging, security, transaction management, and error handling that apply across multiple modules.

- **OOP vs AOP**: While OOP focuses on classes and objects, AOP focuses on "aspects" that cut across these objects.

### 🍽️ Real-World Analogy: The Restaurant
Imagine running a restaurant:
- **Main Service (Business Logic)**: Providing food.
- **Cross-Cutting Concerns**: Billing, logging orders, checking reservations.
- **Without AOP**: You'd manually add billing/logging logic into every single food service method.
- **With AOP**: You define these concerns once and apply them automatically to relevant parts of the service.

---

## Problems Solved by AOP
Without AOP, code suffers from:
1. **Code Duplication**: Common tasks (logging, validation) are implemented in multiple places.
2. **Code Smell**: Core logic is cluttered with "plumbing" code.
3. **Hard to Maintain**: Changing a common task requires modifying many files.
4. **Tight Coupling**: Business logic and infrastructure concerns are inseparable.

---

## AOP Jargon (The Big 5)
1. **Aspect**: A module containing cross-cutting concerns (e.g., `LoggingAspect`).
2. **Join Point**: A specific point during program execution (in Spring AOP, always a **method execution**).
3. **Advice**: The actual "Code" that runs at a join point (e.g., "Log the time").
4. **Pointcut**: An expression that matches join points (defines **where** the advice should run).
5. **Target Object**: The original business class being "advised."

---

## Weaving: Integrating Aspects
Weaving is the process of linking aspects with target objects:
- **Compile-time**: Done during compilation (AspectJ).
- **Load-time**: Done when the class is loaded into the JVM.
- **Runtime**: Done during execution using proxies (Standard **Spring AOP**).

---

## Types of Advices
| Advice Type | Description | Real-World Example |
| :--- | :--- | :--- |
| `@Before` | Runs before the method. | Security/Auth checks. |
| `@AfterReturning` | Runs after successful return. | Logging transaction results. |
| `@AfterThrowing` | Runs if an exception occurs. | Sending alert emails on failure. |
| `@After (Finally)` | Runs regardless of outcome. | Releasing DB connections. |
| `@Around` | Surrounds the method (Most powerful). | Performance timing/caching. |

---

## Implementation in this Scenario
We use an `@Around` advice to track how long a method takes to execute.

### 1. Custom Annotation
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTime { ... }
```

### 2. The Aspect
```java
@Aspect
@Component
public class PerformanceTrackerAspect {
    @Around("@annotation(com.interview.debug.annotation.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // Start Clock -> joinPoint.proceed() -> Stop Clock -> Log
    }
}
```

## Pointcut Expression Cheat Sheet
- `execution(* com.service.*.*(..))`: Any method in the service package.
- `within(com.service..*)`: All methods in package and sub-packages.
- `@annotation(com.annotations.Loggable)`: Only methods with a specific annotation (**Best Practice**).

## Verification Results (Logs)
When you hit the endpoints, check your IDE console/logs to see the AOP flow:

### 1. Success Flow (`/api/scenario27/success-task`)
```text
INFO: [@Before] Security/Auth check before method: successTask()
INFO: [@AfterReturning] Success: successTask() returned: Task processed successfully!
INFO: [@After] Finally block (Resources released) for: successTask()
INFO: [@Around] Performance monitoring recorded for: successTask()
```

### 2. Failure Flow (`/api/scenario27/fail-task`)
```text
INFO: [@Before] Security/Auth check before method: failTask()
ERROR: [@AfterThrowing] Error in method: failTask(). Message: Simulated error in business logic!
INFO: [@After] Finally block (Resources released) for: failTask()
INFO: [@Around] Performance monitoring recorded for: failTask()
```

## Summary for Interviews
- **@Around**: Most powerful, can stop execution or change the result.
- **@Before**: Great for security/auth or data validation.
- **@AfterReturning**: Use for logging successful business transactions.
- **@AfterThrowing**: Perfect for global error alerting/monitoring.
- **@After**: Use for cleaning up resources (closing streams, DB connections).
