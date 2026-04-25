# Circuit Breakers with Resilience4j — Complete Interview Reference

## Table of Contents
1. [What is a Circuit Breaker?](#1-what-is-a-circuit-breaker)
2. [The Three States: Closed, Open, Half-Open](#2-states)
3. [Why Use Circuit Breakers in Microservices?](#3-why-use-it)
4. [Resilience4j vs Hystrix](#4-resilience4j-vs-hystrix)
5. [The Classic Interview Trap: Fallback Method Signatures](#5-the-classic-interview-trap-fallback)
6. [Configuring Failure Rate Thresholds](#6-configuration)
7. [The Sliding Window (Count vs Time based)](#7-sliding-window)
8. [Integrating with Spring Boot Actuator](#8-actuator)
9. [Circuit Breaker vs Retry Pattern](#9-vs-retry)
10. [Bulkheads and Rate Limiters](#10-related-patterns)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Circuit Breaker?
A circuit breaker is a design pattern used to detect failures and encapsulate the logic of preventing a failure from constantly recurring during maintenance, temporary external system failure, or unexpected system difficulties.

---

## 2. The Three States
- **CLOSED**: Requests flow normally. The breaker monitors failures.
- **OPEN**: Failure threshold reached. Requests are blocked immediately and redirected to a fallback.
- **HALF-OPEN**: After a wait duration, the breaker allows a few requests to see if the service has recovered.

---

## 3. Why Use Circuit Breakers in Microservices?
In a microservice chain, if Service C fails, Service B's threads might hang waiting for it, eventually causing Service A to fail. This is a **Cascading Failure**. A circuit breaker stops this by failing fast.

---

## 4. Resilience4j vs Hystrix
- **Hystrix**: Netflix's original library. Now in maintenance mode. Uses its own thread pools.
- **Resilience4j**: The modern standard. Built for Java 8 (functional) and integrates seamlessly with Spring Boot.

---

## 5. The Classic Interview Trap: Fallback Method Signatures
**The Trap**: Your fallback method is never called even though the circuit is open.
**The Fix**: The fallback method MUST have the same parameter list as the original method, PLUS an optional `Throwable` or `Exception` parameter at the end.

---

## 6. Configuring Failure Rate Thresholds
You can configure when the circuit should open (e.g., if 50% of the last 10 requests failed).

---

## 7. The Sliding Window
- **Count-based**: Based on the last N calls.
- **Time-based**: Based on calls in the last N seconds.

---

## 8. Integrating with Spring Boot Actuator
Actuator allows you to monitor the state of your circuit breakers in real-time via `/actuator/circuitbreakers`.

---

## 9. Circuit Breaker vs Retry Pattern
- **Retry**: "Keep trying because the error might be transient."
- **Circuit Breaker**: "Stop trying because the service is definitely down."

---

## 10. Bulkheads and Rate Limiters
- **Bulkhead**: Limits the number of concurrent calls to a service to prevent resource exhaustion.
- **Rate Limiter**: Limits the total number of calls over time.

---

## 11. Common Mistakes
1. Not providing a fallback method (the user still sees an error).
2. Setting the `wait-duration-in-open-state` too low, causing "thrashing."
3. Using the same circuit breaker name for different unrelated services.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a fallback method call another service?**  
A: Yes, but be careful! If that service also fails, you need another fallback.  
**Q: What is the 'Half-Open' state?**  
A: A trial state to check if the downstream service is healthy again.
