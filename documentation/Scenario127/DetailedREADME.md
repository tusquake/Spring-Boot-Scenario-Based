# Caching Strategies — Complete Interview Reference

## Table of Contents
1. [Introduction to Caching](#1-introduction)
2. [Cache-Aside (Lazy Loading)](#2-cache-aside)
3. [Read-Through Caching](#3-read-through)
4. [Write-Through Caching](#4-write-through)
5. [Write-Behind (Write-Back) Caching](#5-write-behind)
6. [The Classic Interview Trap: Cache Invalidation](#6-the-classic-interview-trap-invalidation)
7. [Write-Around Strategy](#7-write-around)
8. [Pros and Cons Comparison Table](#8-comparison)
9. [Handling Data Consistency](#9-consistency)
10. [Choosing the Right TTL](#10-ttl)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Caching is the process of storing copies of data in a high-speed storage layer (like Redis or Memcached) so that future requests can be served faster. The "Strategy" defines how the cache interacts with the primary database.

---

## 2. Cache-Aside
**How it works**: The application first checks the cache. If found (Hit), it returns the data. If not found (Miss), the application fetches from the DB, stores it in the cache, and returns it.
- **Usage**: General purpose, most common strategy.
- **Pros**: Resilience (cache failure doesn't crash the app).

---

## 3. Read-Through
Similar to Cache-Aside, but the library or framework (like a Hibernate Cache provider) handles the fetching and caching automatically. The application code only talks to the cache.

---

## 4. Write-Through
**How it works**: When data is updated, the application writes to the **cache first**, and the cache synchronously updates the database.
- **Pros**: Data in the cache is never stale.
- **Cons**: Write latency is increased because you perform two writes.

---

## 5. Write-Behind (Write-Back)
**How it works**: The application writes only to the cache. The cache then asynchronously updates the database in the background (e.g., every 5 seconds or when a batch of 100 writes is reached).
- **Pros**: Extremely high write performance.
- **Cons**: Risk of data loss if the cache server crashes before the data is persisted to the DB.

---

## 6. The Classic Interview Trap: Dual Writes
**The Trap**: In a Cache-Aside strategy, you update the DB and then update the cache.
**The Problem**: If the cache update fails or if another thread updates the DB in between, the cache and DB will be out of sync.
**The Fix**: Instead of updating the cache, **Delete (Evict)** the cache entry after a DB update. This forces the next read to fetch the latest data from the DB.

---

## 7. Write-Around
Data is written directly to the database, bypassing the cache. The cache is only populated on the first read miss. This prevents the cache from being flooded with data that may never be read again.

---

## 8. Comparison Table
| Strategy | Read Speed | Write Speed | Stale Data? |
|---|---|---|---|
| **Cache-Aside** | Fast (Hit) | Fast | Possible |
| **Write-Through** | Fast | Slower | No |
| **Write-Behind** | Fast | Very Fast | Temporary |

---

## 9. Data Consistency
- **Strong Consistency**: Every read returns the latest write. (Write-Through).
- **Eventual Consistency**: The cache and DB will eventually be in sync. (Write-Behind).

---

## 10. Choosing TTL
TTL (Time To Live) is your insurance policy. Even if your invalidation logic fails, the TTL ensures that stale data eventually disappears. High-frequency data should have short TTLs; static data can have long TTLs.

---

## 11. Common Mistakes
1. Storing massive objects in the cache (wasting memory).
2. Not handling the "N+1" problem when fetching data to populate the cache.
3. Forgetting that Cache-Aside is the default and assuming it's always "Strongly Consistent".

---

## 12. Quick-Fire Interview Q&A
**Q: Which strategy is best for an e-commerce product catalog?**  
A: Cache-Aside with a reasonable TTL (e.g., 1 hour).  
**Q: What is a "Hot Key"?**  
A: A specific cache key (like "Trending_Product_1") that receives a massive percentage of the total traffic, potentially overwhelming the cache node.
