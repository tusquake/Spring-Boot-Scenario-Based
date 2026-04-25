# Performance Monitoring & Profiling — Complete Interview Reference

## Table of Contents
1. [Why Monitor Performance?](#1-why-monitor-performance)
2. [Manual Timing (StopWatch vs System Time)](#2-manual-timing)
3. [Spring's StopWatch Utility](#3-spring-stopwatch-utility)
4. [The Role of AOP in Monitoring](#4-the-role-of-aop-in-monitoring)
5. [The Classic Interview Trap: The "Observer Effect"](#5-the-classic-interview-trap-the-observer-effect)
6. [Best Practices for Production Monitoring](#6-best-practices)
7. [Micrometer and Prometheus Integration](#7-micrometer-and-prometheus)
8. [Standard Response Time (SLA)](#8-standard-response-time)
9. [Profiling Tools (JProfiler, YourKit)](#9-profiling-tools)
10. [JVM Metrics (GC, Heap, Threads)](#10-jvm-metrics)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Why Monitor Performance?

In high-scale systems, code correctness is only half the battle. A service that is correct but takes 10 seconds to respond is functionally broken for the user. Monitoring helps us:
- Identify **bottlenecks** (slow DB queries, nested loops).
- Track **SLAs** (Service Level Agreements).
- Detect **degradation** over time (e.g., after a new deployment).

---

## 2. Manual Timing (StopWatch vs System Time)

### The Basic Way: System.currentTimeMillis()
The most primitive way to time a task. 

```java
long start = System.currentTimeMillis();
doWork();
long end = System.currentTimeMillis();
System.out.println("Time taken: " + (end - start) + "ms");
```
**Pros**: No dependencies.
**Cons**: Noisy code, doesn't handle multiple tasks well, `currentTimeMillis()` is sensitive to system clock changes (NTP). Use `System.nanoTime()` for high precision.

---

## 3. Spring's StopWatch Utility

Spring provides a `StopWatch` class that makes timing multiple tasks cleaner and provides a nice summary.

```java
StopWatch watch = new StopWatch("Report Generation");

watch.start("Task 1: Fetching Data");
service.fetchData();
watch.stop();

watch.start("Task 2: Processing");
service.process();
watch.stop();

System.out.println(watch.prettyPrint());
```

---

## 5. The Classic Interview Trap: The "Observer Effect"

### The Problem
If you add timing logs to every single method in your application, **your application will slow down** because of the monitoring itself. This is known as the Observer Effect (or Heisenbug in debugging).

### The Fix
1.  **Sampling**: Only monitor a percentage of requests (e.g., 1% of total traffic).
2.  **Asynchronous Logging**: Don't let the logging/timing logic block the main thread.
3.  **AOP (Aspect Oriented Programming)**: Use an `@Around` aspect to separate monitoring logic from business logic. This allows you to turn monitoring on/off globally without changing the code.

---

## 11. Common Mistakes

1.  **Monitoring Everything**: Clutters logs and adds overhead.
2.  **Ignoring Garbage Collection**: Sometimes a "slow method" is actually just a thread waiting for a GC pause (Stop-the-World).
3.  **Using System.out.println**: Always use a logger (`log.info`) or a metrics library like Micrometer.
4.  **Not Monitoring DB Latency**: Many "Spring performance issues" are actually slow SQL queries or missing indexes.

---

## 12. Quick-Fire Interview Q&A

**Q: What is the difference between latency and throughput?**
A: **Latency** is how long a single request takes (e.g., 200ms). **Throughput** is how many requests the system can handle per second (e.g., 500 req/sec).

**Q: How would you find a memory leak in a Spring Boot app?**
A: Use a tool like `VisualVM` or `JProfiler` to take a **Heap Dump**. Analyze the dump to see which objects are consuming the most memory and which references are preventing them from being garbage collected.

**Q: What is Micrometer?**
A: Micrometer is a metrics collection library (the "SLF4J for metrics"). It allows you to instrument your code once and push metrics to various backends like Prometheus, New Relic, or Datadog.

**Q: Why use nanoTime() over currentTimeMillis()?**
A: `nanoTime()` is monotonic, meaning it only goes forward and isn't affected by system clock adjustments. `currentTimeMillis()` represents "wall-clock time" and can jump forward or backward.
