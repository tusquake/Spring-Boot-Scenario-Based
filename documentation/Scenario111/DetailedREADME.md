# HTTP Clients Comparison — Complete Interview Reference

## Table of Contents
1. [Introduction to HTTP Clients](#1-introduction)
2. [RestTemplate: The Legacy Standard](#2-rest-template)
3. [WebClient: The Reactive Modern Choice](#3-web-client)
4. [RestClient: The New Fluent Synchronous Client](#4-rest-client)
5. [@HttpExchange: The Declarative Interface](#5-http-exchange)
6. [OpenFeign: The Microservices King](#6-open-feign)
7. [The Classic Interview Trap: Synchronous vs Asynchronous](#7-the-classic-interview-trap-sync-async)
8. [Performance and Thread Management](#8-performance)
9. [Error Handling Comparison](#9-error-handling)
10. [Unit Testing External Calls](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Spring Boot offers multiple ways to call external APIs. Choosing the right one depends on whether your app is blocking (Servlet) or non-blocking (Reactive), and whether you prefer imperative or declarative code.

---

## 2. RestTemplate
The most famous client. It is synchronous and uses a thread-per-request model.
- **Status**: In maintenance mode (deprecated in spirit, but still widely used).
- **Pros**: Simple, familiar, standard.
- **Cons**: Blocking, hard to unit test without `MockRestServiceServer`.

---

## 3. WebClient
Part of Spring WebFlux. It is non-blocking and supports reactive streams (Mono/Flux).
- **Pros**: Highly scalable, can handle thousands of concurrent calls with few threads.
- **Cons**: Steep learning curve (Reactor), can be "overkill" for simple blocking apps.

---

## 4. RestClient (New in Spring 6.1)
The modern replacement for `RestTemplate`. It provides the fluent API of `WebClient` but for synchronous, blocking applications.
- **Pros**: Clean syntax, modern error handling, no need for the `block()` method.

---

## 5. @HttpExchange
A declarative approach introduced in Spring 6. You define an interface, and Spring creates the implementation.
- **Pros**: Extremely clean code, decouples the API definition from the implementation engine.

---

## 6. OpenFeign
The most popular choice in Microservices (Spring Cloud).
- **Pros**: Declarative, built-in support for Load Balancing and Circuit Breakers.
- **Cons**: Requires Spring Cloud dependencies.

---

## 7. The Classic Interview Trap: Blocking in WebClient
**The Trap**: You use `WebClient` in a reactive controller but call `.block()` at the end.
**The Problem**: Calling `.block()` inside a Netty thread will throw an exception or freeze the entire application. It defeats the whole purpose of being reactive.
**The Fix**: Always return `Mono` or `Flux` directly to the controller.

---

## 8. Thread Management
- **RestTemplate**: Uses the main Servlet thread. If the API is slow, the thread is held hostage.
- **WebClient**: Uses a small pool of worker threads. Threads are released while waiting for the network response.

---

## 9. Error Handling
- **RestTemplate**: Uses `ResponseErrorHandler` (often requires checking status codes manually).
- **RestClient/WebClient**: Uses `.onStatus()` or `.onClientError()` which is much more readable and modular.

---

## 10. Testing
- **RestTemplate**: Use `MockRestServiceServer`.
- **WebClient/RestClient**: Use `MockWebServer` (from OkHttp) to simulate a real server response.

---

## 11. Common Mistakes
1. Using `RestTemplate` in a high-concurrency microservice.
2. Forgetting to set timeouts (leading to thread exhaustion).
3. Not sharing the `ObjectMapper` or `HttpClient` across requests (performance drain).

---

## 12. Quick-Fire Interview Q&A
**Q: Which client should I use in a new Spring Boot 3.2 app?**  
A: `RestClient` for blocking apps, `WebClient` for reactive apps.  
**Q: Can WebClient be used in a blocking Spring MVC app?**  
A: Yes, but you will have to call `.block()`, which is less efficient than using `RestClient`.
