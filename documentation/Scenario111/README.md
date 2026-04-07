# Scenario 111: External Client Masterclass 🌐

## Overview
How do you call an external REST API in Spring Boot? 

Historically, this has changed as Spring evolved. From the classic `RestTemplate` to the reactive `WebClient`, and finally the new Spring 6.1 `RestClient` and declarative interfaces.

**Scenario 111** demonstrates all five modern and legacy ways of consuming external APIs, allowing you to compare their syntax and use cases side-by-side.

---

## 🚀 The Five Ways

### 1. RestTemplate (The OG Client) 👴
The original, synchronous client for making HTTP requests.
- **Syntax**: `restTemplate.getForObject(url, Post.class, id)`
- **Best Use Case**: Legacy applications or simple synchronous tasks.

### 2. WebClient (Modern Reactive) ⚡
Introduced in Spring 5. Non-blocking and reactive.
- **Syntax**: `webClient.get().uri("/posts/{id}", id).retrieve().bodyToMono(Post.class)`
- **Best Use Case**: Reactive projects or long-running requests.

### 3. RestClient (Modern Fluent Sync) ✨
Introduced in Spring 6.1 (Spring Boot 3.2). A synchronous client with a fluent API similar to WebClient.
- **Syntax**: `restClient.get().uri("/posts/{id}", id).retrieve().body(Post.class)`
- **Best Use Case**: Traditional, modern synchronous Spring projects.

### 4. Declarative Interface (@HttpExchange) 🪄
Introduced in Spring 6. Cleanest native approach. You define an interface, and Spring generates the proxy.
- **Syntax**: `postClient.getPostById(id)`
- **Best Use Case**: Native Spring projects where WebFlux is present.

### 5. OpenFeign (Spring Cloud) 🛡️
The popular declarative approach for microservices. It has been the standard in the Spring Cloud ecosystem for years.
- **Syntax**: `@FeignClient(...)`
- **Best Use Case**: Enterprise microservice architectures using Spring Cloud.

---

## 🏗️ Implementation Comparison

| Feature | RestTemplate | RestClient | WebClient | @HttpExchange | OpenFeign |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Blocking** | Yes | Yes | Optional | Depends on underlying | Yes |
| **API Style** | Method Overload | Fluent | Fluent | Declarative | Declarative |
| **BOM Required**| No | No | No | No | **Spring Cloud** |

---

## 🧪 How to Test

Using the public **JSONPlaceholder** API:

### 1. Modern Fluent Test (RestClient)
```bash
curl http://localhost:8080/debug-application/api/scenario111/rest-client/1
```

### 2. Declarative Test (@HttpExchange)
```bash
curl http://localhost:8080/debug-application/api/scenario111/http-interface/1
```

### 3. OpenFeign Test
```bash
curl http://localhost:8080/debug-application/api/scenario111/feign/1
```

---

## Interview Tip 💡

**Q**: *"What is the difference between RestTemplate and WebClient?"*  
**A**: *"RestTemplate is synchronous and blocking. WebClient is non-blocking and reactive, much more efficient for high-concurrency loads."*

**Q**: *"What is the coolest new client in Spring Boot 3.2?"*  
**A**: *"The `RestClient`. It gives you the modern fluent API of the WebClient but for synchronous applications."*
