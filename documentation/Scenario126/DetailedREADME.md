# Cache Stampede — Complete Interview Reference

## Table of Contents
1. [What is a Cache Stampede?](#1-definition)
2. [Why Does It Happen? (High Concurrency)](#2-why-it-happens)
3. [Impact on Backend Performance](#3-impact)
4. [Solution 1: Locking/Synchronization](#4-synchronization)
5. [The Classic Interview Trap: Thundering Herd](#5-the-classic-interview-trap-thundering-herd)
6. [Using sync=true in @Cacheable](#6-spring-sync)
7. [Solution 2: Probabilistic Early Recomputation](#7-probabilistic)
8. [Solution 3: Background Refresh (Refresh-Ahead)](#8-refresh-ahead)
9. [Difference between Local vs Distributed Stampede](#9-local-vs-dist)
10. [Testing for Cache Stampede](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Cache Stampede?
A cache stampede (also known as dog-piling) is a failure pattern that occurs when a high-traffic cache item expires. Multiple concurrent requests find the cache empty and all attempt to regenerate the data from the database simultaneously.

---

## 2. Why It Happens
When a key expires, and 100 threads request it at the same microsecond:
1. All 100 threads see `cache.get(key) == null`.
2. All 100 threads call `database.fetch()`.
3. The database is overwhelmed by 100 identical heavy queries.

---

## 3. Impact
- **Database CPU Spike**: The sudden flood of queries can crash the database.
- **Latency Spike**: All users wait for the database response instead of the fast cache response.
- **Cascading Failure**: The slow database can cause connection pool exhaustion in the app server.

---

## 4. Synchronization
The simplest fix is to use a lock. Only the first thread that detects a cache miss is allowed to call the database. Other threads must wait (block) until the first thread finishes and populates the cache.

---

## 5. The Classic Interview Trap: Thundering Herd
**The Trap**: A user asks, *"Is a Cache Stampede the same as a Thundering Herd?"*
**The Answer**: They are similar but not identical.
- **Thundering Herd**: Many processes are woken up by an event (like a new connection), but only one can actually handle it.
- **Cache Stampede**: Many processes try to **perform the same work** because a shared resource (the cache) is missing.

---

## 6. sync=true in Spring
Spring's `@Cacheable` annotation has a `sync` attribute:
`@Cacheable(value = "products", key = "#id", sync = true)`
When `sync` is true, Spring uses a synchronized block to ensure only one thread executes the method. All other threads wait for the result.

---

## 7. Probabilistic Early Recomputation
Instead of waiting for expiry, the cache periodically "decides" to refresh itself slightly before the actual TTL expires, based on a random probability. This spreads out the refresh load and makes it unlikely for many threads to refresh at the exact same time.

---

## 8. Refresh-Ahead
A background task or "sidecar" monitor is responsible for checking the cache TTL. If a key is about to expire, the background task refreshes it. The user always sees a "hot" cache and never has to wait for a database call.

---

## 9. Local vs Distributed
- **Local**: Solved using `synchronized` or `ReentrantLock` within one JVM.
- **Distributed**: Requires a distributed lock (e.g., using **Redis Redlock**) across multiple server instances.

---

## 10. Testing
You can simulate a stampede by using `CompletableFuture.allOf()` to trigger many requests simultaneously against an empty cache and counting how many times the service method was actually executed.

---

## 11. Common Mistakes
1. Not using `sync=true` for heavy queries (like analytics or report generation).
2. Setting the TTL too short on high-traffic items.
3. Not having a "Fallback" or "Stale Data" strategy during a stampede.

---

## 12. Quick-Fire Interview Q&A
**Q: Does sync=true work with all cache providers?**  
A: It depends on the provider implementation, but it is supported by standard providers like Caffeine and Redis.  
**Q: What is the downside of sync=true?**  
A: It can cause threads to block, which might increase latency if the database is also slow, but it's still better than crashing the DB.
