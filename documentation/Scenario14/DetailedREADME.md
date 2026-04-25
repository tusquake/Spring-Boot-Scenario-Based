# API Idempotency — Complete Interview Reference

## Table of Contents
1. [What is Idempotency?](#1-what-is-idempotency)
2. [Why Idempotency Matters in Payments](#2-why-it-matters)
3. [Idempotency Keys (X-Idempotency-Key)](#3-idempotency-keys)
4. [Safe vs Unsafe HTTP Methods](#4-safe-vs-unsafe)
5. [The Classic Interview Trap: Duplicate Transactions](#5-the-classic-interview-trap-duplicate)
6. [Implementing Idempotency with Redis](#6-implementation)
7. [Handling "In-Progress" Requests](#7-handling-in-progress)
8. [Idempotency for Retries (Exponential Backoff)](#8-retries)
9. [Impact on Microservices Architecture](#9-microservices)
10. [Testing for Idempotency](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Idempotency?
An API is **idempotent** if making the same request multiple times results in the exact same state on the server as making it once. 

---

## 2. Why Idempotency Matters in Payments
In a payment system, if a user clicks "Pay" twice or if a network timeout occurs after the payment was processed but before the client received the response, you must ensure the user isn't charged twice.

---

## 3. Idempotency Keys (X-Idempotency-Key)
Clients send a unique value (usually a UUID) in a custom header like `X-Idempotency-Key`. The server uses this key to check if it has already processed this specific request.

---

## 4. Safe vs Unsafe HTTP Methods
- **Safe/Idempotent by Spec**: `GET`, `PUT`, `DELETE`, `HEAD`.
- **Not Idempotent by Spec**: `POST` (typically used for creating new resources).

---

## 5. The Classic Interview Trap: Duplicate Transactions
**The Trap**: A timeout happens. The client retries the `POST` request. Without idempotency, the server creates a second order.
**The Fix**: Check the idempotency key in a cache (like Redis) before processing. If it exists, return the *cached* response from the first request.

---

## 6. Implementing Idempotency with Redis
1. Receive request with Key.
2. Check Redis: `EXISTS Key`.
3. If exists: Return the cached response body.
4. If not: Lock the key, process logic, save response in Redis with TTL, release lock.

---

## 7. Handling "In-Progress" Requests
If a second request arrives while the first one is still being processed, you should return a `409 Conflict` or `425 Too Early`, telling the client to wait.

---

## 8. Idempotency for Retries (Exponential Backoff)
Idempotency is the foundation of a reliable retry strategy. Without it, retrying unsafe operations is dangerous.

---

## 9. Impact on Microservices Architecture
Idempotency is often implemented at the **API Gateway** level or using an **Aspect (AOP)** in Spring to keep business logic clean.

---

## 10. Testing for Idempotency
Use a tool like `JMeter` or `Postman` to fire the same request with the same key 10 times in parallel. Only one should succeed; the others should return the cached response or a conflict error.

---

## 11. Common Mistakes
1. Using a key that is too generic (like `user_id`).
2. Not setting a TTL on idempotency keys in Redis.
3. Returning an error for a duplicate request instead of the original successful response.

---

## 12. Quick-Fire Interview Q&A
**Q: Is PUT idempotent?**  
A: Yes, because replacing a resource with the same state multiple times results in the same final state.  
**Q: What status code should you return for a duplicate?**  
A: Usually the same status code as the original request (e.g., `200 OK` or `201 Created`).
