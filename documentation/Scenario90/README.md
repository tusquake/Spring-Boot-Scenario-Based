# Scenario 90: Service Resilience (Bulkhead Pattern)

## Overview
The **Bulkhead Pattern** is a resilience strategy used to isolate failures in a system. Just like the watertight compartments (bulkheads) in a ship, it ensures that if one part of the system is failing or slow, it doesn't exhaust all resources (threads/CPU) and bring down the entire application.

---

## ⚓ The "Ship Compartment" Theory
Imagine a ship with multiple compartments. If the hull is breached in one compartment, only that section floods. The rest of the ship remains buoyant.
- **Without Bulkheads**: One slow service (e.g., a slow Payment API) can consume all available threads in the Tomcat pool, causing *every* other API in your app to hang.
- **With Bulkheads**: You limit the `slow-service` to a specific number of concurrent threads. Even if it gets stuck, you still have threads left for other services.

---

## ⚔️ Bulkhead Types: Semaphore vs Thread Pool

Resilience4j provides two distinct ways to build a bulkhead. Choosing the right one is a classic **Senior Engineer** interview topic.

| Feature | **Semaphore Bulkhead** (Simple) | **Thread Pool Bulkhead** (Isolated) |
| :--- | :--- | :--- |
| **Execution** | Runs on the **Caller's** thread. | Runs on a **Separate** dedicated pool. |
| **Overhead** | Very Low (just a counter). | Higher (context switching). |
| **Isolation** | **Partial**: Limits concurrency, but caller waits. | **Total**: Caller is independent of pool health. |
| **Best For** | High-volume, already fast services. | Slow, I/O intensive, or risky services. |

> [!IMPORTANT]
> **Technical Requirement**: Resilience4j's `THREADPOOL` bulkhead **MUST** return a `CompletableFuture` (or `CompletionStage`). This is because the execution is delegated to a separate thread pool, and the calling thread needs a future to track the result.

---

## 🧪 Testing the Scenario

### Test 1: Semaphore Bulkhead (Limit: 2)
1. Run 3 simultaneous requests to the Semaphore endpoint:
   ```bash
   curl "http://localhost:8080/debug-application/api/scenario90/test?id=S1" & \
   curl "http://localhost:8080/debug-application/api/scenario90/test?id=S2" & \
   curl "http://localhost:8080/debug-application/api/scenario90/test?id=S3"
   ```
- **Result**: `S1` and `S2` wait 5s. `S3` fails **instantly** with "Semaphore Bulkhead Full".

### Test 2: Thread Pool Bulkhead (Limit: 2 + 1 Queue)
1. Run 4 simultaneous requests to the Thread Pool endpoint:
   ```bash
   curl "http://localhost:8080/debug-application/api/scenario90/test-threadpool?id=T1" & \
   curl "http://localhost:8080/debug-application/api/scenario90/test-threadpool?id=T2" & \
   curl "http://localhost:8080/debug-application/api/scenario90/test-threadpool?id=T3" & \
   curl "http://localhost:8080/debug-application/api/scenario90/test-threadpool?id=T4"
   ```
- **Result**: `T1`, `T2` run in separate threads. `T3` enters the queue. `T4` fails **instantly** with "Thread-Pool is exhausted".

---

## Interview Tip 💡
**Q**: *"Why use a Thread Pool bulkhead if it has more overhead?"*  
**A**: *"Because it provides **Total Isolation**. If the slow service hangs completely, the caller's thread (like a Tomcat worker) isn't being 'held' by the Semaphore. The Thread Pool bulkhead can also use a queue to smooth out small traffic spikes."*

**Q**: *"What is the difference between Circuit Breaker and Bulkhead?"*  
**A**: *"A **Circuit Breaker** stops the boat from calling a broken service (based on failure rate). A **Bulkhead** ensures that even if you *do* call a slow service, it can only use a small part of your boat's resources (based on concurrency)."*
