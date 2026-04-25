# Resilience4j TimeLimiter — Complete Interview Reference

## Table of Contents
1. [What is a TimeLimiter?](#1-what-is-timelimiter)
2. [Why Use TimeLimiter? (Cascading Failures)](#2-why-use-it)
3. [TimeLimiter vs. Thread Timeouts](#3-timelimiter-vs-thread)
4. [Using @TimeLimiter Annotation](#4-using-annotation)
5. [The Classic Interview Trap: Sync vs Async with TimeLimiter](#5-the-classic-interview-trap-sync-async)
6. [Configuring Timeouts in application.yml](#6-configuration)
7. [Fallback Methods for Timeouts](#7-fallback-methods)
8. [Combining TimeLimiter with CircuitBreaker](#8-combining)
9. [Impact of Timeouts on Thread Pools](#9-thread-pool-impact)
10. [Monitoring Timeout Metrics](#10-monitoring)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a TimeLimiter?
A TimeLimiter is a fault-tolerance pattern that restricts the amount of time an operation can take. If the operation does not finish within the specified time, it is canceled, and a `TimeoutException` is thrown.

---

## 2. Why Use TimeLimiter?
In a microservices environment, one slow service can cause a "Cascading Failure". If Service A waits 30 seconds for Service B, and Service A receives many requests, all its threads will be blocked waiting for B. Eventually, Service A will crash. A TimeLimiter prevents this by failing fast.

---

## 3. TimeLimiter vs Thread Timeouts
`TimeLimiter` is designed to work with asynchronous operations (like `CompletableFuture` or `Mono/Flux`). Standard thread timeouts (`thread.join(timeout)`) are blocking and don't fit well into reactive or non-blocking architectures.

---

## 4. Using @TimeLimiter
You apply the annotation to a method that returns a `CompletableFuture` or a reactive type.
```java
@TimeLimiter(name = "backendB")
public CompletableFuture<String> callBackend() { ... }
```

---

## 5. The Classic Interview Trap: Sync Methods
**The Trap**: You apply `@TimeLimiter` to a standard synchronous method (e.g., one that returns `String`).
**The Problem**: It will **NOT WORK**. Resilience4j's TimeLimiter annotation only supports `CompletableFuture`, `Publisher` (Project Reactor), and `Vavr` types. For synchronous methods, you should use a simple `Future.get(timeout)` or a `CircuitBreaker` with a max wait duration.

---

## 6. Configuring Timeouts
You can define timeouts globally or per-service in your configuration:
```yaml
resilience4j.timelimiter:
  instances:
    backendB:
      timeoutDuration: 2s
      cancelRunningFuture: true
```

---

## 7. Fallback Methods
Just like with Circuit Breakers, you can define a fallback method that runs when a timeout occurs, allowing you to return a default or cached response.

---

## 8. Combining with CircuitBreaker
It is a best practice to wrap a `TimeLimiter` inside a `CircuitBreaker`. This way, if a service starts timing out frequently, the circuit breaker will open, preventing even the initial attempt and further protecting the system.

---

## 9. Thread Pool Impact
When a TimeLimiter cancels a future, it doesn't necessarily stop the work in the background immediately (unless `cancelRunningFuture` is true and the task is interruptible). It only stops the caller from waiting.

---

## 10. Monitoring
Resilience4j provides metrics (via Micrometer/Actuator) that track the number of successful calls, timeouts, and failed attempts.

---

## 11. Common Mistakes
1. Setting the timeout too low (causing false positives during minor network jitters).
2. Not defining a fallback, which causes the user to see a raw 500 error.
3. Using it on synchronous methods and expecting it to work.

---

## 12. Quick-Fire Interview Q&A
**Q: Does TimeLimiter stop the background thread?**  
A: Only if `cancelRunningFuture` is set to true and the thread is in an interruptible state (like `Thread.sleep` or I/O).  
**Q: What is the default timeout in Resilience4j?**  
A: 1 second.
