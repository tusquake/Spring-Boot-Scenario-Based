# Declarative HTTP Clients (@HttpExchange) — Complete Interview Reference

## Table of Contents
1. [Introduction to Declarative Clients](#1-introduction)
2. [RestTemplate vs WebClient vs @HttpExchange](#2-comparison)
3. [Defining the Client Interface](#3-defining-interface)
4. [Supported Annotations (@GetExchange, @PostExchange)](#4-annotations)
5. [The Classic Interview Trap: Manual Proxy Configuration](#5-the-classic-interview-trap-proxy)
6. [Passing PathVariables and RequestParams](#6-parameters)
7. [Handling Request Bodies and Headers](#7-body-headers)
8. [Error Handling with Declarative Clients](#8-error-handling)
9. [Integrating with WebClient or RestClient](#9-integration)
10. [Declarative Clients vs Spring Cloud OpenFeign](#10-vs-feign)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Declarative HTTP Clients allow you to define external API calls as simple Java interfaces with annotations. Spring handles the actual implementation (creating the request, parsing the response) at runtime using proxies.

---

## 2. Evolution of Clients
- **RestTemplate**: Synchronous, older, being phased out.
- **WebClient**: Reactive, supports non-blocking calls.
- **@HttpExchange**: (Spring 6+) A standardized, declarative way to use ANY underlying client (WebClient, RestClient, etc.).

---

## 3. Defining the Interface
```java
public interface MyExternalClient {
    @GetExchange("/posts/{id}")
    Post getPost(@PathVariable("id") Long id);
}
```

---

## 4. Supported Annotations
- `@HttpExchange`: Base annotation for the class/interface.
- `@GetExchange`, `@PostExchange`, `@PutExchange`, `@DeleteExchange`, `@PatchExchange`.

---

## 5. The Classic Interview Trap: No Auto-Magic
**The Trap**: You define the interface and `@Autowire` it into your service.
**The Problem**: Unlike Feign, Spring Framework doesn't automatically "find" and implement these interfaces. Your app will fail to start with a `NoSuchBeanDefinitionException`.
**The Fix**: You must manually create the proxy bean in a `@Configuration` class using `HttpServiceProxyFactory`.

---

## 6. Parameters
You use the same annotations you use in controllers: `@PathVariable`, `@RequestParam`, `@RequestHeader`, and `@CookieValue`.

---

## 7. Request Bodies
Just like a controller, you mark the argument with `@RequestBody`. Spring uses the configured `HttpMessageConverters` (like Jackson) to serialize the object to JSON.

---

## 8. Error Handling
If the external API returns a 4xx or 5xx error, the client will throw a `WebClientResponseException` (if using WebClient) or `HttpClientErrorException` (if using RestClient). You can customize this by providing a custom `ResponseErrorHandler`.

---

## 9. Integration
You can choose the engine:
- **RestClientAdapter**: For synchronous blocking calls.
- **WebClientAdapter**: For reactive/non-blocking calls.

---

## 10. vs OpenFeign
- **Feign**: A Netflix project. Very mature, has built-in support for Load Balancing (Ribbon/LoadBalancer) and Circuit Breakers.
- **@HttpExchange**: Native to Spring. Lighter, more flexible, and doesn't require extra dependencies.

---

## 11. Common Mistakes
1. Forgetting to define the Bean Proxy factory (The #1 cause of errors).
2. Using the wrong underlying adapter (e.g., trying to use a blocking RestClient in a reactive WebFlux app).
3. Not handling timeouts correctly in the underlying `WebClient/RestClient` configuration.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @HttpExchange in a non-Spring Boot app?**  
A: Yes, as long as you are using Spring Framework 6+.  
**Q: Does it support binary data (Multipart)?**  
A: Yes, it supports all standard Spring MVC argument types and return types.
