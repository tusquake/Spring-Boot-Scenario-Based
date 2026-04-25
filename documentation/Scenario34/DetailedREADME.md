# CompletableFuture & Parallel Processing — Complete Interview Reference

## Table of Contents
1. [What is CompletableFuture?](#1-what-is-completablefuture)
2. [Why Use CompletableFuture over Future?](#2-why-use-it)
3. [Creating Futures (runAsync vs supplyAsync)](#3-creating-futures)
4. [Chaining Tasks (thenApply vs thenCompose)](#4-chaining-tasks)
5. [The Classic Interview Trap: ForkJoinPool Common Pool](#5-the-classic-interview-trap-forkjoinpool)
6. [Parallel Execution with allOf() and anyOf()](#6-parallel-execution)
7. [Exception Handling in Futures](#7-exception-handling)
8. [Timeouts and Delays in CompletableFuture](#8-timeouts)
9. [Combining Two Futures (thenCombine)](#9-combining-two)
10. [Testing Asynchronous Code](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is CompletableFuture?
Introduced in Java 8, `CompletableFuture` is a class that implements the `Future` interface but adds the ability to chain operations, handle errors, and manually complete the future. It is the core of non-blocking programming in modern Java.

---

## 2. Why Use CompletableFuture?
- **Non-blocking**: You don't have to call `.get()` and wait. You can define what should happen *when* the result is ready.
- **Composable**: Easy to chain multiple async tasks.
- **Readable**: Promotes a functional "pipeline" style of coding.

---

## 3. Creating Futures
- `runAsync(Runnable)`: Run a task that returns no result.
- `supplyAsync(Supplier<T>)`: Run a task that returns a result of type T.

---

## 4. Chaining Tasks
- `thenApply`: Map the result (like `Optional.map`).
- `thenCompose`: Flatten the result (like `Optional.flatMap`). Used when the next task also returns a `CompletableFuture`.

---

## 5. The Classic Interview Trap: ForkJoinPool
**The Trap**: By default, `CompletableFuture` uses the `ForkJoinPool.commonPool()`. 
**The Problem**: This pool is shared by the entire JVM. If one part of your app blocks all threads in the common pool, the rest of your app (including parallel streams) will hang.
**The Fix**: Always provide a custom `Executor` to your `supplyAsync` calls to isolate your thread pools.

---

## 6. Parallel Execution
- `allOf()`: Waits for multiple futures to all finish.
- `anyOf()`: Returns as soon as any one of the futures finishes.

---

## 7. Exception Handling
Use `.exceptionally(ex -> ...)` or `.handle((res, ex) -> ...)` to catch and recover from errors in the async chain without breaking the pipeline.

---

## 8. Timeouts and Delays
In Java 9+, you can use `orTimeout(duration, unit)` to ensure a future fails if it takes too long, or `completeOnTimeout(value, duration, unit)` to return a default value.

---

## 9. Combining Two Futures
`thenCombine()` allows you to wait for two independent futures to finish and then process their results together.

---

## 10. Testing Asynchronous Code
Use `CompletableFuture.get(timeout)` in your test cases to wait for the result, or use libraries like **Awaitility** for more expressive async testing.

---

## 11. Common Mistakes
1. Calling `.join()` or `.get()` too early, turning async code back into synchronous code.
2. Not handling exceptions, causing failures to disappear silently.
3. Forgetting that `thenApply` runs on the same thread as the previous task unless `thenApplyAsync` is used.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between join() and get()?**  
A: `get()` throws checked exceptions; `join()` throws unchecked exceptions (easier for lambda use).  
**Q: How many threads are in the common ForkJoinPool?**  
A: Typically `Total Cores - 1`.
