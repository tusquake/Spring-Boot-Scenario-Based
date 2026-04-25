# Web-Aware Bean Scopes — Complete Interview Reference

## Table of Contents
1. [Introduction to Web Scopes](#1-introduction)
2. [Request Scope](#2-request-scope)
3. [Session Scope](#3-session-scope)
4. [Application Scope](#4-application-scope)
5. [The Classic Interview Trap: Proxy Mode](#5-the-classic-interview-trap-proxy)
6. [Injecting Scoped Beans into Singletons](#6-injection-problem)
7. [WebSocket Scope](#7-websocket-scope)
8. [Using ObjectProvider for Lazy Resolution](#8-object-provider)
9. [Cleaning Up Scoped Beans (Destruction)](#9-destruction)
10. [Web Scopes in Reactive (WebFlux)](#10-webflux-scopes)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to Web Scopes
In addition to `singleton` and `prototype`, Spring provides specialized scopes for web applications. These allow you to store data that is tied to the lifecycle of a specific HTTP request or user session.

---

## 2. Request Scope
A bean defined with `@RequestScope` is created for every single HTTP request. It is destroyed as soon as the request is finished. This is perfect for storing request-specific metadata like a `TraceID` or a `UserAgent`.

---

## 3. Session Scope
A bean defined with `@SessionScope` is tied to an individual HTTP Session. It lives as long as the session exists. This is ideal for storing user-specific data like a **Shopping Cart** or **User Profile**.

---

## 4. Application Scope
`@ApplicationScope` is similar to a singleton, but its lifecycle is tied to the `ServletContext` rather than the Spring `ApplicationContext`. In a standard Spring Boot app, they are effectively the same.

---

## 5. The Classic Interview Trap: Proxy Mode
**The Trap**: You inject a `@RequestScope` bean into a `@RestController` (which is a `Singleton`).
**The Problem**: A Singleton is created only once at startup. How can it hold a reference to a Request bean that doesn't exist yet and changes every second?
**The Fix**: You must use **Scoped Proxies**. Spring injects a "Proxy" object into the Controller. When you call a method on the proxy, it internally looks up the "real" bean for the current request.
`@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)`

---

## 6. Injection via ObjectProvider
If you don't want to use proxies, you can inject an `ObjectProvider<T>`. This allows you to fetch the "current" instance of the scoped bean manually inside your method:
`MyRequestBean bean = provider.getObject();`

---

## 7. WebSocket Scope
Spring also supports a `websocket` scope, where a bean lives for the duration of a specific WebSocket session.

---

## 8. State in Scoped Beans
Because Request and Session beans are "thread-local" (tied to a specific user/request), they are inherently safer to store state in than Singletons. However, you still need to be careful with Session beans in a clustered environment (see Spring Session).

---

## 9. Destruction Lifecycle
- Request beans are destroyed when the request completes.
- Session beans are destroyed when the session times out or is invalidated.
- **Note**: Prototype beans are NOT managed by Spring after they are created; their destruction is up to the GC.

---

## 10. Web Scopes in WebFlux
**Warning**: Standard Web Scopes (Request/Session) are **NOT supported** in Spring WebFlux (Reactive). In WebFlux, you use the `Context` object provided by Project Reactor to propagate data through the reactive stream.

---

## 11. Common Mistakes
1. Forgetting to set `proxyMode` when injecting into a Singleton.
2. Storing too much data in the Session scope (causes memory issues).
3. Using Request scope for data that doesn't actually change per request (better as a Singleton).

---

## 12. Quick-Fire Interview Q&A
**Q: What is the default proxy mode for @RequestScope?**  
A: In Spring Boot, the `@RequestScope` composition annotation automatically sets the proxy mode to `TARGET_CLASS`.  
**Q: Can I use @SessionScope in a stateless JWT-based API?**  
A: Technically yes, but it will force the creation of an HTTP Session, defeating the purpose of being stateless.
