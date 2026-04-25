# Async Controller Communication — Complete Interview Reference

## Table of Contents
1. [Introduction to Asynchronous MVC](#1-introduction)
2. [Why Async? (Servlet Thread Liberation)](#2-why-async)
3. [CompletableFuture in Controllers](#3-completable-future)
4. [DeferredResult: Manual Async Control](#4-deferred-result)
5. [Callable: A Simpler Async Alternative](#5-callable)
6. [The Classic Interview Trap: Blocked Servlet Threads](#6-the-classic-interview-trap-blocking)
7. [Spring Events (Fire-and-Forget)](#7-spring-events)
8. [Configuring Async Task Executors](#8-executors)
9. [Timeout Handling in Async Requests](#9-timeouts)
10. [Interceptors for Async Requests](#10-interceptors)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Standard Spring MVC uses a "Thread-per-request" model. Asynchronous communication allows you to "liberate" the servlet thread while a long-running task is happening, allowing the server to handle more concurrent requests.

---

## 2. Why Async?
If you have 200 servlet threads and you call a slow API that takes 10 seconds, after 200 requests, your entire server is frozen. Async processing allows the servlet thread to go back to the pool to help other users while the 10-second task continues in the background.

---

## 3. CompletableFuture
Spring MVC recognizes `CompletableFuture<T>` as a return type. It will automatically wait for the future to complete before sending the response to the client, without blocking the main worker thread.

---

## 4. DeferredResult
`DeferredResult` is a handle that you return immediately. You can then set the result from ANY thread (even a thread not managed by Spring) at any time. This is the foundation for features like **Long Polling** and **Chat Applications**.

---

## 5. Callable
`Callable<T>` is the simplest way to do async. Spring will execute the logic inside the callable in a separate thread and return the result. It is basically a wrapper for a standard synchronous call that you want to move out of the servlet thread.

---

## 6. The Classic Interview Trap: Thread Starvation
**The Trap**: You use `@Async` but don't configure a custom `TaskExecutor`.
**The Problem**: By default, Spring uses a `SimpleAsyncTaskExecutor`, which creates a new thread for EVERY request. In a high-load scenario, this will lead to an `OutOfMemoryError` or massive CPU context-switching overhead.
**The Fix**: Always define a `ThreadPoolTaskExecutor` with sensible limits (core/max pool size and queue capacity).

---

## 7. Spring Events
Used for "Fire-and-Forget" scenarios. The controller publishes an event and returns a response to the user immediately. An `@EventListener` marked with `@Async` picks up the event and processes it in the background.

---

## 8. Executors
You must enable async support in your config:
```java
@Configuration
@EnableAsync
public class AsyncConfig { ... }
```

---

## 9. Timeout Handling
Async requests should always have a timeout. If the background task never finishes, the client shouldn't wait forever. 
`deferredResult.onTimeout(() -> { ... });`

---

## 10. Interceptors
Standard `HandlerInterceptors` don't work correctly with async requests. You must use `AsyncHandlerInterceptor` which provides an `afterConcurrentHandlingStarted` method.

---

## 11. Common Mistakes
1. Using async for very fast operations (The overhead of thread switching makes it slower).
2. Forgetting that `SecurityContext` is NOT propagated to async threads by default.
3. Not handling exceptions inside the `CompletableFuture`.

---

## 12. Quick-Fire Interview Q&A
**Q: Does async communication make a single request faster?**  
A: No, it actually makes a single request slightly slower due to overhead. It improves the **throughput** of the whole server.  
**Q: What is the difference between DeferredResult and WebAsyncTask?**  
A: `WebAsyncTask` allows you to specify the timeout and executor directly for that specific request.
