# Scenario 127: Caching Strategies (Aside, Through, Behind)

This scenario demonstrates the three most common caching patterns used in modern application architecture and how to implement them in Spring Boot.

## High-Level Concepts

### 1. Cache Aside (Lazy Loading)
- **Concept**: The application is responsible for the cache. It checks the cache first; if it's a "miss," it fetches data from the DB, stores it in the cache, and then returns it.
- **Spring Implementation**: `@Cacheable`.
- **Pros**: Only requested data is cached. Resilience against cache failure (app hits DB instead).

### 2. Write Through (Synchronous)
- **Concept**: Data is written to the cache and the database synchronously in the same transaction.
- **Spring Implementation**: `@CachePut` on a save/update method.
- **Pros**: Cache and DB are always in sync. High read performance for recently written data.

### 3. Write Behind (Asynchronous / Write Back)
- **Concept**: Data is written to the cache immediately, and the database update is offloaded to a background process.
- **Spring Implementation**: Manual cache update + `@Async` task or a message queue.
- **Pros**: Extremely low latency for write operations. Reduces DB pressure by decoupling the write from the response.

---

## How to Test this Scenario

### 1. Test Write Through (Sync)
Updates the DB first, then updates the cache. The response will wait for the DB.
`POST http://localhost:8080/debug-application/api/scenario127/write-through/1?name=SyncProduct`

### 2. Test Write Behind (Async)
Updates the cache and returns **immediately**. The DB is updated 5 seconds later in the background.
`POST http://localhost:8080/debug-application/api/scenario127/write-behind/1?name=AsyncProduct`

**Observation**: Notice the response time in your client; it should be near 0ms, while the console logs will show the DB update starting and completing 5 seconds later.

### 3. Verify Cache vs DB State
Checks what is currently in the Cache vs what is actually in the DB.
`GET http://localhost:8080/debug-application/api/scenario127/verify/1`

---

## 🚀 The Fix: Avoiding Self-Invocation Pitfalls

During implementation, we encountered a common Spring issue: **@Async and @Transactional methods called from within the same class do not work** (because the proxy is bypassed). 

We solved this by creating a dedicated `Scenario127AsyncService` to handle the background database operations.

---

## Interview Tip: "When should I use Write Behind?"
Use it for **high-write** systems where immediate consistency is not strictly required. Example: Social media "likes" or "view counts". If the database is slightly behind the cache, users won't notice, but your write throughput will be much higher.
