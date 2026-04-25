# Spring WebFlux & Reactive Programming — Complete Interview Reference

## Table of Contents
1. [What is Reactive Programming?](#1-what-is-reactive)
2. [Spring MVC vs Spring WebFlux](#2-mvc-vs-webflux)
3. [The Event Loop Model (Netty)](#3-event-loop)
4. [Mono vs Flux](#4-mono-vs-flux)
5. [The Classic Interview Trap: Blocking in a Reactive Chain](#5-the-classic-interview-trap-blocking)
6. [Backpressure in Reactive Streams](#6-backpressure)
7. [Schedulers and Thread Management](#7-schedulers)
8. [Server-Sent Events (SSE)](#8-sse)
9. [Reactive Repositories (R2DBC, MongoDB)](#9-reactive-repos)
10. [Error Handling in WebFlux](#10-error-handling)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Reactive Programming?
Reactive programming is a declarative programming paradigm concerned with data streams and the propagation of change. In Java, this is primarily implemented using the **Project Reactor** library.

---

## 2. Spring MVC vs Spring WebFlux
- **Spring MVC**: One thread per request. Blocks if the thread waits for I/O (e.g., a DB query).
- **Spring WebFlux**: Event-loop based. Small number of threads handle many requests by never blocking.

---

## 3. The Event Loop Model (Netty)
WebFlux typically runs on **Netty**. Instead of waiting for a slow DB call, the thread registers a callback and moves on to handle another request. When the DB is ready, the event loop triggers the callback to finish the original request.

---

## 4. Mono vs Flux
- **Mono<T>**: Emits 0 or 1 element. Used for single results (like fetching a user by ID).
- **Flux<T>**: Emits 0 to N elements. Used for streams of data (like a list of items or a real-time feed).

---

## 5. The Classic Interview Trap: Blocking in a Reactive Chain
**The Trap**: Calling a blocking method (like `Thread.sleep()` or a JDBC call) inside a WebFlux method.
**The Problem**: Since WebFlux uses a very small number of threads (equal to CPU cores), blocking even one thread can significantly degrade performance. 
**The Fix**: Use `subscribeOn(Schedulers.boundedElastic())` for blocking calls, or better yet, use fully reactive drivers (R2DBC).

---

## 6. Backpressure in Reactive Streams
Backpressure is the ability of a consumer to tell the producer to "slow down" if it is sending data faster than it can be processed. This prevents `OutOfMemoryError` in stream processing.

---

## 7. Schedulers and Thread Management
- `Schedulers.immediate()`: Current thread.
- `Schedulers.parallel()`: Fixed pool for CPU-intensive work.
- `Schedulers.boundedElastic()`: Dynamic pool for I/O-intensive work.

---

## 8. Server-Sent Events (SSE)
WebFlux makes it easy to stream data to the browser using `MediaType.TEXT_EVENT_STREAM_VALUE`. The browser keeps the connection open and receives data as it is produced by the `Flux`.

---

## 9. Reactive Repositories
To be fully reactive, the database driver must also be non-blocking. **R2DBC** (Reactive Relational Database Connectivity) is the standard for SQL databases in the reactive world.

---

## 10. Error Handling in WebFlux
Use operators like `onErrorResume`, `onErrorReturn`, or `switchIfEmpty` to handle errors in a functional, non-blocking style.

---

## 11. Common Mistakes
1. Using `Thread.sleep()` in a WebFlux controller.
2. Forgetting that a Flux/Mono does nothing until you **subscribe** to it.
3. Mixing blocking JDBC with non-blocking WebFlux without a dedicated scheduler.

---

## 12. Quick-Fire Interview Q&A
**Q: Is WebFlux always faster than MVC?**  
A: No. For simple, low-concurrency apps, MVC is often faster due to less overhead. WebFlux shines in high-concurrency, I/O-bound scenarios.  
**Q: What is a marble diagram?**  
A: A visual representation of how a reactive operator (like `map` or `filter`) transforms a stream of elements over time.
