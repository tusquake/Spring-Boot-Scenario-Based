# Scenario 112: Asynchronous Communication Masterclass 🚀

## Overview
How do you handle long-running tasks without blocking your users or your server? 

Spring Boot provides several powerful tools for asynchronous and non-blocking execution. **Scenario 112** demonstrates the most critical patterns: `@Async`, `CompletableFuture`, `DeferredResult`, and **Asynchronous Events**.

---

## ⚡ The Async Patterns

### 1. @Async with Managed Thread Pools 🧵
The most common way to offload work.
- **Syntax**: `@Async("executorName")`
- **Key Caveat**: Never use the default executor! Always define a `ThreadPoolTaskExecutor` to manage your resources.
- **Why?**: Prevents creating a new thread for every request, which could crash your app under load.

### 2. CompletableFuture ⏩
A modern way to return a result that will be available later.
- **Syntax**: `CompletableFuture<T> getAsyncResult()`
- **Benefit**: Spring MVC automatically handles the transition, releasing the Tomcat thread while wait for the computation to finish.

### 3. DeferredResult ⏳
Provides manual control over the response.
- **Syntax**: `DeferredResult<T> result = new DeferredResult<>(timeout)`
- **Benefit**: Excellent for long-polling (waiting for an external event to happen) or manual thread management.

### 4. Async Events (Fire-and-Forget) ✉️
Decouples the request from the side effects (like sending emails or logging).
- **Syntax**: `eventPublisher.publishEvent(myEvent)`
- **Benefit**: The user gets a response immediately, while the event is processed in the background on a different thread.

---

## ⚠️ The "Self-Invocation" Trap
One of the most common **Senior Engineer** interview questions.

**Problem**: If you call an `@Async` method from another method in the same class, it runs **synchronously**.
**Why?**: `@Async` works through AOP Proxies. Calling a method internally bypasses the proxy.
**Fix**: Call the `@Async` method from a different `@Service` or `@Controller`.

---

## 🧪 How to Test

### 1. Test CompletableFuture (Wait for Result)
```bash
curl http://localhost:8080/debug-application/api/scenario112/completable-future?input=Hello-Async
```
Check the console logs to see the `Scenario112-` thread processing the result.

### 2. Test DeferredResult (Wait for Resolution)
```bash
curl http://localhost:8080/debug-application/api/scenario112/deferred-result
```
Observe the 3-second delay on the client side, while the server thread is free in the background.

### 3. Test Fire-and-Forget Events
```bash
curl http://localhost:8080/debug-application/api/scenario112/trigger-event?message=Async-Event-Demo
```
You get an immediate response, while the log shows the event being handled a few seconds later.

---

## Interview Tip 💡

**Q**: *"What is the difference between @Async and @EventListener?"*  
**A**: *"@Async is for method-level execution on a separate thread. @EventListener is for decoupling logic. However, you can combine them: by adding @Async to an @EventListener, you make the event handling non-blocking for the publisher."*

**Q**: *"Why shouldn't you use the default ThreadPool in Spring @Async?"*  
**A**: *"The default `SimpleAsyncTaskExecutor` doesn't reuse threads. For every call, it spawns a new thread. In an interview, always mention that you define a `ThreadPoolTaskExecutor` with a `QueueCapacity` to prevent thread exhaustion."*
