# Scenario 39: Virtual Threads (Project Loom)

## Overview
Traditionally, Spring Boot uses a **One-Thread-Per-Request** model. If you have 200 platform threads and they all block on a database call, your server can't handle the 201st request.

**Virtual Threads** (introduced in Java 21) are lightweight threads managed by the JVM, not the OS. They allow you to handle thousands (or even millions) of concurrent blocking requests with minimal memory overhead.

---

## 🚀 Enabling Virtual Threads
In Spring Boot 3.2+, you don't need complex `WebFlux` code to be scalable. You just need one property in `application.properties`:

```properties
spring.threads.virtual.enabled=true
```

When this is enabled:
1. **Tomcat** uses virtual threads to handle HTTP requests.
2. **@Async** methods use virtual threads.
3. **Task Schedulers** use virtual threads.

---

## 🛠️ The Implementation
In this scenario, we simulate a "blocking" operation (like a slow API call or DB query).

```java
@GetMapping("/blocking")
public String slowTask() throws InterruptedException {
    Thread.sleep(1000); // Simulates blocking I/O
    return "Done by: " + Thread.currentThread();
}
```

If virtual threads are enabled, `Thread.currentThread()` will show something like `VirtualThread[#45,...]`.

---

## 🧪 Testing the Scenario

1. **Verify Virtual Threads**:
```bash
curl "http://localhost:8080/debug-application/api/scenario39/info"
```

2. **Simulate Load**:
Under standard threads, if you hit a `/blocking` endpoint with 500 concurrent users, the server would likely crawl or reject connections. With Virtual Threads, it handles them effortlessly.

---

## Interview Tip 💡
**Q**: *"Does Project Loom make my code faster?"*
**A**: *"No, it makes it more **scalable (higher throughput)**, not faster (lower latency). A single `Thread.sleep(1000)` still takes 1000ms. However, instead of 200 threads handling 200 requests/sec, you can now have 10,000 requests/sec sitting in a blocked state without crashing the server."*
