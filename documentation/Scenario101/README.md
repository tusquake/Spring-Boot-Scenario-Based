# Scenario 101: Declarative HTTP Clients (@HttpExchange) 🚀

## Overview
How do you call an external REST API without writing repetitive `RestTemplate` or `WebClient` code?

Historically, developers used **OpenFeign**, which required adding a heavy dependency and was not a part of core Spring. In **Spring Boot 3.2+**, we have a native, built-in alternative: **Declarative HTTP Clients**. 

This allow us to define an external service's contract using an **Interface** and `@HttpExchange` annotations. Spring then generates the implementation at runtime.

---

## 🚀 Key Features

### 1. Interface-First Design
You define the HTTP methods, paths, and payloads directly in an interface. This decouples your business logic from the specific HTTP client being used.

### 2. Built-in Integration
It works seamlessly with both **WebClient** (Reactive) and the new **RestClient** (Synchronous), allowing you to choose the underlying engine easily.

---

## 🏗️ Implementation Details

### 1. The @HttpExchange Interface (`Scenario101Client.java`)
```java
@HttpExchange("/posts")
public interface Scenario101Client {
    @GetExchange("/{id}")
    Scenario101Model getPostById(@PathVariable("id") Long id);
}
```

### 2. The Proxy Configuration (`Scenario101Config.java`)
We create the implementation using a factory:
```java
HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build().createClient(Scenario101Client.class);
```

---

## 🧪 How to Test

### 1. Fetch All Posts
Call our proxy to fetch the full list of mock posts from JSONPlaceholder:
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario101/posts"
```

### 2. Fetch a Single Post
Fetches a specific post by ID (verified via an external call):
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario101/posts/1"
```

### 3. Verification
Check the response. You should see real data coming from the JSONPlaceholder mock API, even though we never manually wrote a single line of "Fetch" or "Execute" code!

---

## Interview Tip 💡
**Q**: *"What is the difference between @HttpExchange and OpenFeign?"*  
**A**: *"@HttpExchange is a native Spring Framework feature (no extra dependencies required). OpenFeign is a third-party project by Netflix that requires Spring Cloud. @HttpExchange also allows you to choose between WebClient, RestTemplate, or the new RestClient as the underlying executor."*

**Q**: *"When would you use a declarative client over a manual RestTemplate?"*  
**A**: *"Declarative clients are much cleaner when you have a large external API with many endpoints. It separates the 'What' (the API contract) from the 'How' (the HTTP execution logic), making the code easier to read and test."*
