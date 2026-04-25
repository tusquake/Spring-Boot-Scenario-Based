# Distributed Tracing — Complete Interview Reference

## Table of Contents
1. [What is Distributed Tracing?](#1-what-is-distributed-tracing)
2. [Trace ID vs Span ID](#2-trace-vs-span)
3. [Spring Cloud Sleuth vs Micrometer Tracing](#3-sleuth-vs-micrometer)
4. [B3 Propagation and W3C TraceContext](#4-propagation)
5. [The Classic Interview Trap: Missing Traces in Async Threads](#5-the-classic-interview-trap-async)
6. [Visualizing Traces with Zipkin or Jaeger](#6-visualization)
7. [Log Correlation (Trace ID in Logs)](#7-log-correlation)
8. [Sampling Rates (Performance vs Visibility)](#8-sampling)
9. [Tracing across Microservices (RestTemplate/WebClient)](#9-cross-service)
10. [Custom Spans and Annotations (@NewSpan)](#10-custom-spans)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Distributed Tracing?
Distributed tracing is a method used to profile and monitor applications, especially those built using microservices architecture. It tracks a single request as it moves through multiple services, helping identify where latency or errors occur.

---

## 2. Trace ID vs Span ID
- **Trace ID**: A unique identifier for an entire end-to-end request (remains constant across all services).
- **Span ID**: A unique identifier for a single operation within a service (changes as the request moves between services or internal methods).

---

## 3. Spring Cloud Sleuth vs Micrometer Tracing
- **Sleuth**: The standard for Spring Boot 2.x.
- **Micrometer Tracing**: The new standard for Spring Boot 3.x, replacing Sleuth. It uses the Micrometer Observation API.

---

## 4. Propagation Patterns
To keep the Trace ID consistent across services, it must be "propagated" in HTTP headers.
- **B3**: Developed by Zipkin (headers like `X-B3-TraceId`).
- **W3C**: The newer industry standard (`traceparent` header).

---

## 5. The Classic Interview Trap: Missing Traces in Async Threads
**The Trap**: When you use `@Async`, the Trace ID is lost in the background thread.
**The Fix**: Use a `LazyTraceExecutor` or a `ContextPropagatingExecutorService` to ensure the tracing context is copied to the new thread.

---

## 6. Visualizing Traces with Zipkin or Jaeger
Trace data is often sent to a collector like **Zipkin** or **Jaeger**. These tools provide a UI to see a "waterfall" view of the request, showing exactly how long each service took.

---

## 7. Log Correlation
Spring automatically adds the Trace ID and Span ID to the SLF4J **MDC (Mapped Diagnostic Context)**, which means they will automatically appear in your log lines if configured correctly.

---

## 8. Sampling Rates
In production, you usually don't trace 100% of requests (too much data). You might set `sampling.probability=0.1` (10%).

---

## 9. Tracing across Microservices
Spring automatically intercepts `RestTemplate`, `WebClient`, and `Feign` calls to inject the tracing headers.

---

## 10. Custom Spans and Annotations
You can create your own sub-spans within a service using the `@NewSpan` annotation or the programmatic API to get more granular timing data.

---

## 11. Common Mistakes
1. Not including the Trace ID in the logs.
2. Setting sampling to 100% in a high-traffic production environment.
3. Forgetting to propagate headers when using a non-Spring HTTP client.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between a Trace and a Span?**  
A: A Trace is the whole journey; a Span is a single step in that journey.  
**Q: How does tracing help in debugging?**  
A: It allows you to search for a specific `requestId` across 50 different microservice log files instantly.
