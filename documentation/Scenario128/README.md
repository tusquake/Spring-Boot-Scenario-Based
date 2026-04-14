# Scenario 128: In-Memory Caching with Caffeine

Caffeine is a high-performance, near-optimal Java caching library. It is the default in-memory cache provider for Spring Boot 2.x and 3.x, replacing Guava.

## High-Level Concepts

### 1. TTL (Time To Live)
- **Concept**: Data is automatically removed from the cache after a fixed duration since it was created or last updated.
- **Config**: `.expireAfterWrite(Duration.ofSeconds(10))`.

### 2. Size-Based Eviction
- **Concept**: The cache has a maximum capacity. When full, it uses an advanced eviction algorithm (Window TinyLFU) to remove entries.
- **Config**: `.maximumSize(100)`.

### 3. Statistics & Monitoring
- **Concept**: Caffeine can track performance metrics like hit rate, miss rate, and eviction count.
- **Config**: `.recordStats()`.

---

## How to Test this Scenario

### 1. Test TTL Expiration (10s)
1. Call the endpoint: `GET http://localhost:8080/debug-application/api/scenario128/ttl/R1`
2. Call it again quickly. Notice the timestamp stays the same (Cache Hit).
3. Wait 11 seconds.
4. Call it again. Notice the timestamp updates (Cache Miss/Expired).

### 2. Test Size-Based Eviction (Limit 2)
1. Call `/size/A`
2. Call `/size/B`
3. Call `/size/C` (This forces an eviction of one of the previous items).
4. Check the `/stats` endpoint to see the `evictionCount` increase.

### 3. Monitoring Stats
View real-time hit/miss/eviction data:
`GET http://localhost:8080/debug-application/api/scenario128/stats`

---

## 🚀 Key Configurations

In this scenario, we defined two specialized `CacheManager` beans in `CacheConfig.java` to demonstrate different Caffeine properties:

```java
// Manager 1: Short TTL
Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(10)).recordStats();

// Manager 2: Limited Size
Caffeine.newBuilder().maximumSize(2).recordStats();
```

> [!IMPORTANT]
> When using multiple `CacheManager` beans, you must mark one as `@Primary` or use the `cacheManager` attribute in the `@Cacheable` annotation to specify which one to use.

---

## Interview Tip: "Caffeine vs Redis?"
- **Caffeine**: Local, in-memory, extremely fast (nanoseconds), but data is lost on restart and not shared across instances.
- **Redis**: Distributed, shared across all instances, slower (latency over network), but persistent and scalable.
- **Best Practice**: Use Caffeine for static or reference data that rarely changes, and Redis for dynamic session or shared data.
