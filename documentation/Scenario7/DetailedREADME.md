# Spring Async & Context Propagation — Complete Interview Reference

## Table of Contents
1. [What is @Async?](#1-what-is-async)
2. [How @Async Works (Proxies & Thread Pools)](#2-how-it-works)
3. [The Context Propagation Problem](#3-the-context-propagation-problem)
4. [SecurityContext in Async Threads](#4-securitycontext)
5. [The Classic Interview Trap: Missing Context in Async](#5-the-classic-interview-trap-missing-context)
6. [Fixes for Context Propagation](#6-fixes)
7. [ThreadLocal vs InheritableThreadLocal](#7-threadlocal)
8. [Configuring Async Executors](#8-configuring-executors)
9. [Return Types: Future, CompletableFuture, and void](#9-return-types)
10. [Exception Handling in @Async](#10-exception-handling)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is @Async?
The `@Async` annotation allows a method to be executed in a separate thread. When a method is marked `@Async`, the caller does not wait for it to finish; it returns immediately, and execution continues in the background.

---

## 2. How @Async Works (Proxies & Thread Pools)
Spring uses AOP proxies to intercept calls to `@Async` methods. When you call an async method, the proxy submits the task to a `TaskExecutor` (a thread pool) and returns control to the caller.

---

## 3. The Context Propagation Problem
Most Spring features (Security, Transactions, Sleuth/Tracing) use `ThreadLocal` variables to store data for the current thread. Since `@Async` starts a **new** thread, it doesn't automatically inherit the `ThreadLocal` values from the original thread.

---

## 4. SecurityContext in Async Threads
The `SecurityContextHolder` uses a `ThreadLocal` by default. This means that inside an `@Async` method, `SecurityContextHolder.getContext().getAuthentication()` will return `null`.

---

## 5. The Classic Interview Trap: Missing Context in Async
**The Scenario**: You try to fetch the logged-in user's details inside an async task to generate a personalized email.
**The Trap**: The code works in your local unit test but fails in production (returning `null`) because the security context didn't propagate to the background thread.

---

## 6. Fixes for Context Propagation
1. **DelegatingSecurityContextAsyncTaskExecutor**: Wrap your `TaskExecutor` so it copies the `SecurityContext` to every new task it runs.
2. **Manual Passing**: Pass the `userId` or `Authentication` object as a parameter to the `@Async` method.

---

## 7. ThreadLocal vs InheritableThreadLocal
`ThreadLocal` is strictly for one thread. `InheritableThreadLocal` allows child threads to inherit values from the parent thread. However, this often fails with **Thread Pools** because threads are reused, potentially leading to "context leaking" between different users' requests.

---

## 8. Configuring Async Executors
Always define a custom `ThreadPoolTaskExecutor`. 
- **Core Pool Size**: Minimum threads always running.
- **Max Pool Size**: Maximum threads allowed under load.
- **Queue Capacity**: How many tasks to buffer before starting new threads.

---

## 9. Return Types: Future, CompletableFuture, and void
- **void**: Use for fire-and-forget tasks (no result needed).
- **Future<T>**: Old way to handle results.
- **CompletableFuture<T>**: Modern, non-blocking way to handle results and chain tasks.

---

## 10. Exception Handling in @Async
If the method returns `void`, you must implement `AsyncUncaughtExceptionHandler` to catch errors. If it returns `Future`, exceptions are captured inside the future object and thrown when you call `.get()`.

---

## 11. Common Mistakes
1. Calling an `@Async` method from within the same class (proxy won't trigger).
2. Using the default `SimpleAsyncTaskExecutor` in production (it doesn't reuse threads).
3. Forgetting `@EnableAsync`.

---

## 12. Quick-Fire Interview Q&A
**Q: Can @Async be used with @Transactional?**  
A: Yes, but the transaction will NOT be shared. The async method will start its own new transaction.  
**Q: Why do we need @EnableAsync?**  
A: It tells Spring to look for `@Async` annotations and create the necessary proxies.
