# Graceful Shutdown in Spring Boot — Complete Interview Reference

## Table of Contents
1. [What is Graceful Shutdown?](#1-what-is-graceful-shutdown)
2. [Hard Shutdown vs Graceful Shutdown](#2-hard-vs-graceful)
3. [Enabling Graceful Shutdown (server.shutdown)](#3-enabling)
4. [Timeout Per Shutdown Phase](#4-timeout-phase)
5. [The Classic Interview Trap: The Long-Running Task](#5-the-classic-interview-trap-long-running)
6. [How Tomcat/Undertow Handles Graceful Shutdown](#6-how-it-works)
7. [Termination Signals (SIGTERM vs SIGKILL)](#7-termination-signals)
8. [Graceful Shutdown in Kubernetes](#8-kubernetes)
9. [Impact on Active Transactions](#9-active-transactions)
10. [Testing Graceful Shutdown Locally](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Graceful Shutdown?
Graceful shutdown is a mechanism that allows a web server to stop accepting new requests while giving existing, "in-flight" requests a specific amount of time to finish before the application finally stops.

---

## 2. Hard Shutdown vs Graceful Shutdown
- **Hard Shutdown (Immediate)**: All active requests are abruptly terminated. The user sees a "Connection Refused" or "500 Error." Data might be left in a partially processed state.
- **Graceful Shutdown**: The server stops taking new traffic but waits for active threads to finish their work (up to a timeout).

---

## 3. Enabling Graceful Shutdown (server.shutdown)
In Spring Boot 2.3+, you can enable this with a single property:
```properties
server.shutdown=graceful
```

---

## 4. Timeout Per Shutdown Phase
You must also specify how long the server should wait for active requests.
```properties
spring.lifecycle.timeout-per-shutdown-phase=30s
```

---

## 5. The Classic Interview Trap: The Long-Running Task
**The Trap**: You have a request that takes 20 seconds to finish. You configured a graceful shutdown timeout of 10 seconds.
**The Result**: If you shut down the server while that request is running, it will still be killed after 10 seconds. 
**The Fix**: Ensure your shutdown timeout is always slightly longer than your longest expected request.

---

## 6. How Tomcat/Undertow Handles Graceful Shutdown
The embedded container (Tomcat, Jetty, or Undertow) handles the actual suspension of the network connector. Once suspended, the container waits for the `Executor` to drain its active threads.

---

## 7. Termination Signals (SIGTERM vs SIGKILL)
- **SIGTERM (15)**: The "polite" request to stop. Spring Boot catches this and triggers the graceful shutdown.
- **SIGKILL (9)**: The "brutal" kill. The OS kills the process immediately, bypassing any graceful shutdown logic.

---

## 8. Graceful Shutdown in Kubernetes
In K8s, when a Pod is being deleted, it sends a `SIGTERM` to the container. If your Spring Boot app doesn't shut down gracefully, you might lose transactions during a "rolling update."

---

## 9. Impact on Active Transactions
Graceful shutdown protects the *request* thread, but it doesn't automatically wait for background `@Async` tasks or scheduled jobs unless they are also configured to shut down gracefully.

---

## 10. Testing Graceful Shutdown Locally
1. Start the app.
2. Trigger a long-running request (e.g., 15 seconds).
3. Send a `Ctrl+C` (SIGINT) or use `kill <pid>` (SIGTERM) to the app.
4. Observe the logs: It should say "Commencing graceful shutdown" and wait for the request to finish before stopping.

---

## 11. Common Mistakes
1. Setting the shutdown timeout too low (lower than the longest request).
2. Not enabling `server.shutdown=graceful` in production.
3. Forgetting that external resources (like DB connections) might also close during shutdown, potentially breaking the very request you are trying to save.

---

## 12. Quick-Fire Interview Q&A
**Q: Does Spring Boot enable graceful shutdown by default?**  
A: No. The default is `immediate`.  
**Q: Which containers support graceful shutdown in Spring Boot?**  
A: Tomcat, Jetty, Undertow, and Netty (WebFlux).
