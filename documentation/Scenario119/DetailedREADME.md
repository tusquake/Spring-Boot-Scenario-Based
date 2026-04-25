# Advanced MockMvc Testing — Complete Interview Reference

## Table of Contents
1. [What is MockMvc?](#1-what-is-mockmvc)
2. [Testing Status Codes and Return Types](#2-status-codes)
3. [Testing Request Headers and Cookies](#3-headers-cookies)
4. [Testing Query Parameters and Path Variables](#4-params)
5. [The Classic Interview Trap: Testing @RequestBody (JSON)](#5-the-classic-interview-trap-json)
6. [Expectations for JSON Content (JsonPath)](#6-json-path)
7. [Testing Controller Advice & Exception Handlers](#7-exception-handling)
8. [Testing File Uploads with MockMvc](#8-file-uploads)
9. [Async Request Testing (asyncDispatch)](#9-async-testing)
10. [Performance of MockMvc vs RestTemplate](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is MockMvc?
MockMvc is the main entry point for server-side Spring MVC test support. It allows you to execute requests against a mock Servlet environment and perform assertions on the response without starting a real HTTP server.

---

## 2. Status Codes
You can assert any HTTP status code:
`mockMvc.perform(get("/api/test")).andExpect(status().isOk());`
`mockMvc.perform(get("/api/admin")).andExpect(status().isForbidden());`

---

## 3. Headers and Cookies
```java
mockMvc.perform(get("/api/test")
    .header("X-Custom", "Value")
    .cookie(new Cookie("session", "123")))
    .andExpect(header().string("Content-Type", "application/json"));
```

---

## 4. Params and Path Variables
- **Path**: Just include it in the URL: `get("/api/users/10")`.
- **Params**: Use the `.param("key", "value")` method.

---

## 5. The Classic Interview Trap: JSON Serialization
**The Trap**: A developer tries to send a Java object directly into `.content()`.
**The Problem**: `.content()` expects a `String` or a `byte[]`. It does not automatically call Jackson to convert your object to JSON.
**The Fix**: You must manually serialize the object using an `ObjectMapper` before passing it to MockMvc:
```java
String json = new ObjectMapper().writeValueAsString(user);
mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json));
```

---

## 6. JsonPath Expectations
Instead of asserting the whole string, use `jsonPath` to verify specific fields:
`andExpect(jsonPath("$.name").value("John"))`
`andExpect(jsonPath("$.roles[0]").value("ADMIN"))`

---

## 7. Exception Handling
MockMvc is the best way to test your `@ControllerAdvice`. You can trigger a request that causes an exception and verify that the correct error JSON and status code are returned.

---

## 8. File Uploads
Use `MockMvcRequestBuilders.multipart("/upload")` along with `MockMultipartFile` to simulate a file upload request.

---

## 9. Async Testing
Testing an async controller is a two-step process:
1. Perform the initial request.
2. Call `.andExpect(request().asyncStarted())`.
3. Call `.andReturn().getAsyncResult()` or use `asyncDispatch()` to verify the final result.

---

## 10. Performance
MockMvc is significantly faster than `@SpringBootTest(webEnvironment = RANDOM_PORT)` because it bypasses the network stack and the actual HTTP server (Tomcat/Netty).

---

## 11. Common Mistakes
1. Not setting the `contentType(MediaType.APPLICATION_JSON)` for POST requests.
2. Forgetting that `jsonPath` indexes are 0-based.
3. Hardcoding the whole JSON string in an assertion (makes tests brittle to field order changes).

---

## 12. Quick-Fire Interview Q&A
**Q: Can MockMvc test a real running server?**  
A: No. MockMvc only works with a Mock Servlet environment. To test a real server, use `TestRestTemplate` or `WebTestClient`.  
**Q: What is the purpose of andDo(print())?**  
A: It prints the entire Request and Response (headers, body, etc.) to the console, which is incredibly useful for debugging failed tests.
