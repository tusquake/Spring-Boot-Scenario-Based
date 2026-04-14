# Scenario 126: Cache Stampede Problem & Prevention

The **Cache Stampede** (also known as dog-piling) is a performance failure that happens when a high-traffic cache key expires, and multiple concurrent requests for that key all hit the "slow" database simultaneously to recompute the result.

## High-Level Concepts

### 1. The Stampede (The Problem)
When a heavily accessed key (e.g., product details for a flash sale) expires, every incoming request sees a cache miss. In a high-traffic system, 100+ requests might hit the service at the exact same millisecond. Each one decides to "help" by fetching the data from the database, leading to a sudden, massive spike in DB load.

### 2. Double-Checked Locking / sync=true (The Solution)
Spring Boot provides a native solution using the `sync` attribute in the `@Cacheable` annotation. This ensures that only **one thread** is allowed to compute the value while other concurrent threads for the same key are blocked until the result is available in the cache.

---

## How to Test this Scenario

### Step 1: Trigger the Vulnerable Stampede
Fire 5 concurrent requests to the vulnerable endpoint:
`POST http://localhost:8080/debug-application/api/scenario126/trigger/vulnerable/126`

**Observe the Logs & Response**: 
- You will see 5 "Database Hit" logs in the console.
- Response will show `databaseHits: 5`.
- All 5 requests took ~2 seconds simultaneously, but none waited for each other.

### Step 2: Trigger the Secured Prevention
Fire 5 concurrent requests to the secured endpoint:
`POST http://localhost:8080/debug-application/api/scenario126/trigger/secured/126`

**Observe the Logs & Response**:
- You will see exactly **ONE** "Database Hit" log.
- Response will show `databaseHits: 1`.
- The first request hits the DB, while the other 4 block and wait. Once the first one finishes, the others are served from the cache immediately.

### Step 3: Check Current Stats
`GET http://localhost:8080/debug-application/api/scenario126/stats`

### Step 4: Reset
`POST http://localhost:8080/debug-application/api/scenario126/reset`

---

## 🚀 The Fix: Implementation Details

The fix involves a simple addition to your `@Cacheable` annotation:

```java
// VULNERABLE
@Cacheable(value = "products", key = "#id")
public Product getProduct(String id) { ... }

// SECURED
@Cacheable(value = "products", key = "#id", sync = true)
public Product getProduct(String id) { ... }
```

> [!IMPORTANT]
> While `sync = true` is highly effective, remember that it blocks threads. For extremely high-concurrency scenarios, you might also consider **Probabilistic Early Recomputation** or **Distributed Locks** (using Redis/Redisson) if you are in a multi-node cluster.

---

## Interview Tip: "When should I use sync=true?"
Use it for **expensive** or **high-latency** computations that are also **high-traffic**. If the computation is fast (e.g., 5ms), the overhead of synchronization might not be worth it. If it's slow (e.g., 2s DB query), `sync = true` is a life-saver for your database.
