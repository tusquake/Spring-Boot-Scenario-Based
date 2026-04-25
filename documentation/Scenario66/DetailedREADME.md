# Spring Retry (@Retryable) — Complete Interview Reference

## Table of Contents
1. [What is Spring Retry?](#1-what-is-spring-retry)
2. [Why Use Retry? (Transient Failures)](#2-why-retry)
3. [Enabling Retry (@EnableRetry)](#3-enabling)
4. [The @Retryable Annotation](#4-retryable-annotation)
5. [The Classic Interview Trap: Internal Method Calls (Proxies)](#5-the-classic-interview-trap-proxies)
6. [Backoff Strategies (Fixed vs Exponential)](#6-backoff)
7. [Recovery Logic with @Recover](#7-recover)
8. [Configuring Max Attempts and Exceptions](#8-configuration)
9. [RetryTemplate (Programmatic Approach)](#9-retrytemplate)
10. [Stateful vs Stateless Retry](#10-stateful-retry)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Spring Retry?
Spring Retry is a library that provides the ability to automatically re-invoke a failed operation. This is particularly useful for dealing with transient errors, such as network glitches or database deadlocks.

---

## 2. Why Use Retry?
In a distributed system, things fail. Instead of immediately returning an error to the user, you can retry a call to a downstream service 2 or 3 times, which often resolves the issue if the failure was temporary.

---

## 3. Enabling Retry
You must add `@EnableRetry` to one of your configuration classes for the `@Retryable` annotations to be processed.

---

## 4. The @Retryable Annotation
You apply this to a method. You can specify which exceptions should trigger a retry:
```java
@Retryable(retryFor = {RemoteAccessException.class}, maxAttempts = 3)
public String callApi() { ... }
```

---

## 5. The Classic Interview Trap: Proxies
**The Trap**: Like `@Transactional` and `@Async`, `@Retryable` works via AOP Proxies. 
**The Problem**: If you call a `@Retryable` method from another method in the same class, the retry logic will **NOT** trigger.
**The Fix**: Call the method from a different bean.

---

## 6. Backoff Strategies
- **Fixed**: Wait a constant time between retries (e.g., 1 second).
- **Exponential**: Double the wait time after each failure (1s, 2s, 4s). This is better for allowing a struggling system to recover.

---

## 7. Recovery Logic
If all retry attempts fail, you can define a "fallback" method using `@Recover`. This method must have the same return type and a matching exception parameter.
```java
@Recover
public String recover(RemoteAccessException e) {
    return "Fallback Response";
}
```

---

## 8. Configuring Max Attempts
By default, Spring Retry attempts the operation 3 times. You can customize this and also define which exceptions should NOT trigger a retry (`noRetryFor`).

---

## 9. RetryTemplate
For more control, you can use `RetryTemplate`. This is useful if you want to apply retry logic inside a loop or based on complex conditions that annotations can't handle.

---

## 10. Stateful vs Stateless Retry
- **Stateless**: (Default) The retry happens immediately within the same thread.
- **Stateful**: The exception is propagated, and the retry happens in a subsequent call (useful for message-driven systems where you want the message to go back to the queue).

---

## 11. Common Mistakes
1. Retrying non-idempotent operations (e.g., a POST request that creates a payment).
2. Not defining an exponential backoff, which can overwhelm a failing downstream service ("Retry Storm").
3. Forgetting that `@Recover` methods must be in the same class as the `@Retryable` method.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @Retryable on a Controller method?**  
A: Yes, but it is better practice to use it on the Service layer where the actual failure (I/O) happens.  
**Q: What is the default backoff time?**  
A: 1 second.
