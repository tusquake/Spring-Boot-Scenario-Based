# Resilience4j Bulkhead — Complete Interview Reference

## Table of Contents
1. [What is the Bulkhead Pattern?](#1-what-is-the-bulkhead-pattern)
2. [Ship Metaphor: How it Prevents Sinking](#2-ship-metaphor)
3. [Semaphore-based Bulkhead](#3-semaphore-bulkhead)
4. [Fixed Thread-Pool Bulkhead](#4-threadpool-bulkhead)
5. [The Classic Interview Trap: Bulkhead vs RateLimiter](#5-the-classic-interview-trap-comparison)
6. [Configuring Bulkhead in Spring Boot](#6-configuration)
7. [Bulkhead and Async Processing](#7-async)
8. [Combining Bulkhead with CircuitBreaker](#8-combining)
9. [Monitoring Bulkhead Metrics](#9-monitoring)
10. [Choosing the Right Max Concurrent Calls](#10-tuning)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is the Bulkhead Pattern?
The Bulkhead pattern is used to isolate resources in a system. It ensures that a failure or slowdown in one component does not consume all the resources (like threads or memory) of the entire system, preventing "Cascading Failures".

---

## 2. The Ship Metaphor
In a ship, a bulkhead is a wall that divides the hull into multiple watertight compartments. If one compartment is breached, the water is contained, and the ship stays afloat. In software, if one service is slow, the Bulkhead ensures it only uses a limited number of threads, leaving the rest of the threads available for other services.

---

## 3. Semaphore-based Bulkhead
- **How it works**: Uses a counter (Semaphore). If the counter reaches zero, further requests are rejected.
- **Usage**: Best for blocking calls (Standard REST/JDBC).
- **Pros**: Low overhead.

---

## 4. Fixed Thread-Pool Bulkhead
- **How it works**: Uses a dedicated thread pool for the service. Requests are queued or rejected if the pool is full.
- **Usage**: Best for asynchronous operations.
- **Pros**: Provides true isolation (a slow task can't block the main thread).

---

## 5. The Classic Interview Trap: Bulkhead vs RateLimiter
**The Trap**: A user asks, *"Isn't Bulkhead just a Rate Limiter?"*
**The Answer**: **NO**.
- **RateLimiter**: Controls how many requests are allowed in a **period of time** (e.g., 10 per second).
- **Bulkhead**: Controls how many requests are allowed **at the same time** (concurrency).

---

## 6. Configuring in Spring Boot
```yaml
resilience4j.bulkhead:
  instances:
    backendA:
      maxConcurrentCalls: 10
      maxWaitDuration: 0 # Reject immediately if full
```

---

## 7. Bulkhead and Async
When using the Thread-Pool variant, your method should return a `CompletableFuture`. Resilience4j will manage the execution inside its isolated pool.

---

## 8. Combining with CircuitBreaker
It is a best practice to use both. The **Bulkhead** protects the *caller* from thread exhaustion, while the **CircuitBreaker** protects the *downstream service* from being overwhelmed.

---

## 9. Monitoring
Actuator provides metrics like `resilience4j.bulkhead.available_concurrent_calls`. You should set up alerts if this value stays at 0 for long periods.

---

## 10. Tuning
Choosing the `maxConcurrentCalls` depends on your downstream service capacity. If the service can only handle 100 requests per second, and each request takes 1 second, your bulkhead should probably be set to 100 or less.

---

## 11. Common Mistakes
1. Setting the `maxWaitDuration` too high (causes callers to block for a long time, defeating the purpose of the bulkhead).
2. Not using a dedicated thread pool for non-blocking operations.
3. Ignoring the "Bulkhead Full" errors in your logs.

---

## 12. Quick-Fire Interview Q&A
**Q: What happens when the bulkhead is full?**  
A: A `BulkheadFullException` is thrown, which you can catch in a fallback method.  
**Q: Can I share a bulkhead across multiple methods?**  
A: Yes, you can use the same instance name for multiple methods to limit their collective concurrency.
