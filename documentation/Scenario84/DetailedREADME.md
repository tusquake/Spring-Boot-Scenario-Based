# HandlerInterceptor — Complete Interview Reference

## Table of Contents
1. [What is a HandlerInterceptor?](#1-what-is-handlerinterceptor)
2. [Interceptor vs Filter](#2-interceptor-vs-filter)
3. [The Three Methods: preHandle, postHandle, afterCompletion](#3-methods)
4. [Registering Interceptors (WebMvcConfigurer)](#4-registration)
5. [The Classic Interview Trap: Interceptor vs Filter for Security](#5-the-classic-interview-trap-security)
6. [Short-circuiting the Request (preHandle return false)](#6-short-circuiting)
7. [Modifying the Model with postHandle](#7-modifying-model)
8. [Resource Cleanup in afterCompletion](#8-cleanup)
9. [Mapping Interceptors to Specific URLs](#9-mapping)
10. [Using Multiple Interceptors (Execution Order)](#10-order)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is HandlerInterceptor?
A `HandlerInterceptor` allows you to intercept incoming HTTP requests and outgoing responses within the Spring MVC lifecycle. It sits between the `DispatcherServlet` and your `Controller`.

---

## 2. Interceptor vs Filter
- **Filter**: Part of the Servlet container. It runs **BEFORE** the request even reaches Spring MVC. It has no knowledge of Spring-specific things like Controllers or Method Annotations.
- **Interceptor**: Part of Spring MVC. It has access to the "Handler" (the Controller method) and can perform logic based on method annotations.

---

## 3. The Three Methods
1. **preHandle**: Runs before the controller. Can block the request by returning `false`.
2. **postHandle**: Runs after the controller, but **BEFORE** the view is rendered. You can modify the `ModelAndView` here.
3. **afterCompletion**: Runs after the entire request is finished (view rendered). Used for cleaning up resources (like closing DB connections).

---

## 4. Registration
Interceptors must be registered in a `WebMvcConfigurer` implementation:
```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new MyCustomInterceptor()).addPathPatterns("/api/**");
}
```

---

## 5. The Classic Interview Trap: Where does Security live?
**The Question**: *"Why is Spring Security implemented as a Filter and not an Interceptor?"*
**The Answer**: Because security should be the very first layer. If a request is malicious, it should be blocked before it even enters the Spring MVC infrastructure. Additionally, Filters can protect non-Spring resources (like static HTML files), whereas Interceptors only work for requests mapped to a `HandlerMapping`.

---

## 6. Short-circuiting
If `preHandle` returns `false`, the controller is never called. This is useful for:
- Checking API Keys.
- Simple rate limiting.
- Logging request start times.

---

## 7. postHandle
In a REST API, `postHandle` is less useful because `@ResponseBody` writes directly to the response stream before `postHandle` is even called. It is primarily used in traditional JSP/Thymeleaf applications.

---

## 8. afterCompletion
This method is guaranteed to run even if an exception occurred in the controller (unlike `postHandle`). It is the best place to clear `ThreadLocal` variables (like MDC context).

---

## 9. Mapping
You can use `addPathPatterns` and `excludePathPatterns` to precisely control which URLs should be intercepted.

---

## 10. Execution Order
- **preHandle**: Executed in the order they are registered (1, 2, 3).
- **postHandle/afterCompletion**: Executed in **REVERSE** order (3, 2, 1).

---

## 11. Common Mistakes
1. Trying to read the `RequestBody` inside an interceptor (The input stream can only be read once; once the interceptor reads it, the controller gets an empty body).
2. Forgetting that `afterCompletion` runs even after an error.
3. Not handling `ThreadLocal` cleanup, leading to memory leaks in thread pools.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I autowire beans into an Interceptor?**  
A: Yes, if you define the interceptor as a `@Bean` or `@Component`.  
**Q: What is the benefit of Interceptor over AOP?**  
A: Interceptors have direct access to the `HttpServletRequest` and `HttpServletResponse` objects, which AOP aspects do not usually have.
