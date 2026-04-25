# Caffeine Cache (Local Caching) — Complete Interview Reference

## Table of Contents
1. [What is Caffeine?](#1-what-is-caffeine)
2. [Caffeine vs Guava](#2-caffeine-vs-guava)
3. [Key Features (TTL, Max Size, Stats)](#3-features)
4. [Eviction Policies (W-TinyLFU)](#4-eviction-policies)
5. [The Classic Interview Trap: Soft vs Weak References](#5-the-classic-interview-trap-references)
6. [Configuring Caffeine in Spring Boot](#6-configuration)
7. [Refresh vs Expire](#7-refresh-vs-expire)
8. [Monitoring with CacheStats](#8-monitoring)
9. [Async Cache Support](#9-async-support)
10. [Handling Cache Misses with CacheLoader](#10-cache-loader)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Caffeine?
Caffeine is a high-performance, near-optimal Java caching library. It is the successor to Guava's cache and is the default local cache implementation used by Spring Boot.

---

## 2. Caffeine vs Guava
Caffeine is significantly faster than Guava because it uses a more modern approach to concurrency and memory management. It also uses the **W-TinyLFU** algorithm, which provides a higher hit rate than standard LRU (Least Recently Used).

---

## 3. Key Features
- **Size-based eviction**: Automatically remove items when the cache reaches a maximum number of entries or total weight.
- **Time-based expiration**: `expireAfterWrite` (TTL) or `expireAfterAccess`.
- **Reference-based eviction**: Garbage collect entries when there are no more references to the keys or values.

---

## 4. W-TinyLFU Algorithm
This is the secret sauce of Caffeine. It maintains a small "window" for newly added items and uses a frequency-based counter to decide which items deserve to stay in the long-term cache. This protects the cache from being "flushed" by a single scan of rarely-used data.

---

## 5. The Classic Interview Trap: Time Accuracy
**The Trap**: A user asks, *"Does an item expire exactly at the 10-minute mark?"*
**The Answer**: **NO**. Caffeine is a "passive" cache. Expiration checks happen during write operations or occasionally during read operations. If the cache is not being accessed, the expired items might sit in memory longer. This is done to avoid having a dedicated background thread for every cache instance.

---

## 6. Spring Configuration
```yaml
spring.cache.type: caffeine
spring.cache.caffeine.spec: initialCapacity=100,maximumSize=500,expireAfterWrite=5m,recordStats
```

---

## 7. Refresh vs Expire
- **Expire**: The entry is deleted. The next request must wait for the DB.
- **Refresh**: The entry is still available to users (stale), while a background thread fetches the new value from the DB. This provides much lower latency for high-traffic items.

---

## 8. Monitoring
Calling `.recordStats()` allows you to see the hit count, miss count, and eviction count. These should be exported to Prometheus to monitor your cache efficiency.

---

## 9. Async Cache
Caffeine supports `AsyncCache`, which returns a `CompletableFuture`. This allows you to perform non-blocking cache lookups in reactive applications.

---

## 10. CacheLoader
Instead of manually calling `put()`, you can provide a `CacheLoader` function. When a key is missing, Caffeine will automatically call this function to fetch and store the data, ensuring that multiple threads don't try to load the same key at once (Atomic loading).

---

## 11. Common Mistakes
1. Not setting a `maximumSize`, leading to an `OutOfMemoryError`.
2. Using `expireAfterAccess` when you really meant `expireAfterWrite` (The former can keep data alive forever if it's accessed frequently).
3. Not recording stats, making it impossible to tune the cache size.

---

## 12. Quick-Fire Interview Q&A
**Q: Is Caffeine a distributed cache?**  
A: No, it is a local (in-memory) cache. For distributed caching, use Redis or Hazelcast.  
**Q: What is the benefit of recordStats()?**  
A: It provides visibility into the "Hit Ratio". If your hit ratio is only 5%, your cache is likely not helping and is just wasting memory.
