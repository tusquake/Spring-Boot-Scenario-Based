# Mapped Diagnostic Context (MDC) Tracing — Complete Interview Reference

## Table of Contents
1. [What is MDC (Mapped Diagnostic Context)?](#1-what-is-mdc)
2. [How MDC Works (ThreadLocal)](#2-how-it-works)
3. [The Correlation ID Pattern](#3-correlation-id)
4. [MDC in Spring Boot Filters/Interceptors](#4-mdc-filters)
5. [The Classic Interview Trap: MDC in Async Threads](#5-the-classic-interview-trap-async)
6. [MDC and Logback Configuration](#6-logback-config)
7. [MDC vs Spring Cloud Sleuth](#7-mdc-vs-sleuth)
8. [Clearing the MDC (Memory Leak Prevention)](#8-clearing-mdc)
9. [Contextual Logging in Multi-tenant Apps](#9-multi-tenant)
10. [Performance Impact of MDC](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is MDC?
MDC (Mapped Diagnostic Context) is a map-like structure provided by logging frameworks (SLF4J/Logback) that allows you to store contextual information for the current thread and automatically include it in every log message.

---

## 2. How MDC Works
MDC internally uses a `ThreadLocal` map. This means that any value you put into the MDC is private to the current thread and can be accessed by the logger without passing it through method parameters.

---

## 3. The Correlation ID Pattern
The most common use case for MDC is a **Correlation ID**. You generate a unique UUID at the beginning of a request (in a Filter) and put it in the MDC. Every log line generated during that request will then contain that ID.

---

## 4. MDC in Spring Boot Filters
The best place to manage MDC is in a `HandlerInterceptor` or a `OncePerRequestFilter`.
```java
MDC.put("traceId", UUID.randomUUID().toString());
try {
    chain.doFilter(request, response);
} finally {
    MDC.clear(); // Always clear in finally block!
}
```

---

## 5. The Classic Interview Trap: MDC in Async Threads
**The Trap**: You start an `@Async` task or a `CompletableFuture`. The Trace ID is **NOT** visible in the background thread's logs.
**Why?**: `ThreadLocal` values are not inherited by new threads by default.
**The Fix**: You must manually copy the MDC map to the new thread or use a decorator for your `TaskExecutor`.

---

## 6. MDC and Logback Configuration
To see the MDC values in your logs, you must update your log pattern in `logback.xml` using the `%X{key}` syntax:
`%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} [%X{traceId}] - %msg%n`

---

## 7. MDC vs Spring Cloud Sleuth
- **MDC**: A low-level logging tool. You manually manage the IDs.
- **Sleuth/Micrometer**: A high-level tracing framework. It automatically handles MDC management, propagation across microservices, and integration with Zipkin.

---

## 8. Clearing the MDC
**CRITICAL**: Because threads are reused in a thread pool (like Tomcat's worker pool), if you don't call `MDC.clear()` at the end of a request, the next request handled by that same thread will "inherit" the old Trace ID, leading to confusing logs.

---

## 9. Contextual Logging in Multi-tenant Apps
MDC is great for multi-tenant applications to log the `tenantId` or `userId` for every operation, helping support teams isolate issues for a specific customer.

---

## 10. Performance Impact
Putting values into a `ThreadLocal` map is very fast. The main cost is the string formatting during logging, which is minimal compared to the benefit of having searchable traces.

---

## 11. Common Mistakes
1. Forgetting to clear the MDC in a `finally` block.
2. Putting large objects into the MDC (it should only contain small strings).
3. Expecting MDC to work across microservice boundaries (it only works within one JVM).

---

## 12. Quick-Fire Interview Q&A
**Q: Is MDC thread-safe?**  
A: Yes, because it uses `ThreadLocal`, each thread has its own isolated map.  
**Q: Can I store a User object in MDC?**  
A: No, MDC only accepts `String` values. Convert your ID or name to a string first.
