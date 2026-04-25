# API Rate Limiting — Complete Interview Reference

## Table of Contents
1. [What is Rate Limiting?](#1-what-is-rate-limiting)
2. [Why Use Rate Limiting?](#2-why-use-rate-limiting)
3. [Common Algorithms (Token Bucket vs Leaky Bucket)](#3-algorithms)
4. [Fixed Window vs Sliding Window](#4-window-types)
5. [The Classic Interview Trap: Throttling in Distributed Systems](#5-the-classic-interview-trap-distributed)
6. [Implementing Rate Limiting with Bucket4j](#6-implementation)
7. [Global vs Per-User Rate Limiting](#7-global-vs-user)
8. [Handling Rate Limit Exceeded (429 Too Many Requests)](#8-handling-429)
9. [Redis-backed Rate Limiting](#9-redis-backed)
10. [Client-side Rate Limiting (Retry-After)](#10-client-side)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Rate Limiting?
Rate limiting is a strategy for limiting network traffic. It puts a cap on how often someone can repeat an action within a certain timeframe – for instance, trying to log in to an account.

---

## 2. Why Use Rate Limiting?
- **Security**: Protect against Brute Force and DoS (Denial of Service) attacks.
- **Cost**: Prevent runaway processes from consuming expensive cloud resources.
- **Fairness**: Ensure one "heavy" user doesn't degrade the experience for everyone else.

---

## 3. Common Algorithms (Token Bucket vs Leaky Bucket)
- **Token Bucket**: Tokens are added to a bucket at a fixed rate. Each request takes a token. If the bucket is empty, the request is rejected. (Supports bursts).
- **Leaky Bucket**: Requests enter a bucket and "leak" out at a fixed rate. If the bucket is full, new requests are discarded. (Smooths out traffic).

---

## 4. Fixed Window vs Sliding Window
- **Fixed Window**: 100 requests allowed from 1:00 to 1:01. (Susceptible to spikes at the window boundary).
- **Sliding Window**: 100 requests allowed in any rolling 60-second period. (More accurate but harder to implement).

---

## 5. The Classic Interview Trap: Throttling in Distributed Systems
**The Trap**: You implement rate limiting in-memory on one server. 
**The Problem**: If you have 10 server instances, a user could actually make 10x the allowed limit by hitting different instances.
**The Fix**: Use a centralized store like **Redis** to keep track of the limits across all instances.

---

## 6. Implementing Rate Limiting with Bucket4j
`Bucket4j` is a popular Java library for rate limiting. You can integrate it into Spring using a `Filter` or an `Interceptor`.

---

## 7. Global vs Per-User Rate Limiting
- **Global**: Limit total traffic to the API.
- **Per-User**: Limit based on IP address, API Key, or User ID.

---

## 8. Handling Rate Limit Exceeded (429 Too Many Requests)
When a user exceeds the limit, return a `429 Too Many Requests` status code. Include a `Retry-After` header telling the client when they can try again.

---

## 9. Redis-backed Rate Limiting
Using Redis (specifically `INCR` and `EXPIRE` commands) allows you to implement a fast, shared rate limiter that works across a cluster of microservices.

---

## 10. Client-side Rate Limiting (Retry-After)
Good API clients should respect the `Retry-After` header and implement **Exponential Backoff** to avoid hammering the server as soon as the limit resets.

---

## 11. Common Mistakes
1. Not having a separate limit for anonymous vs authenticated users.
2. Using a time window that is too large (making it hard for users to recover).
3. Ignoring the performance overhead of the rate limiter itself.

---

## 12. Quick-Fire Interview Q&A
**Q: What HTTP status code is used for rate limiting?**  
A: `429 Too Many Requests`.  
**Q: What is "Burstiness" in rate limiting?**  
A: The ability of an algorithm (like Token Bucket) to allow a sudden spike of requests as long as tokens are available in the bucket.
