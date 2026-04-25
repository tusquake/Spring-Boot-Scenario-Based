# Servlet Filters & OncePerRequestFilter — Complete Interview Reference

## Table of Contents
1. [What is a Servlet Filter?](#1-what-is-a-filter)
2. [How the Filter Chain Works](#2-filter-chain)
3. [OncePerRequestFilter vs Filter](#3-onceperrequestfilter)
4. [The Three Methods: init, doFilter, destroy](#4-methods)
5. [The Classic Interview Trap: Filter Order (@Order)](#5-the-classic-interview-trap-order)
6. [Modifying Requests and Responses (Wrappers)](#6-modifying-wrappers)
7. [Registering Filters (@WebFilter vs FilterRegistrationBean)](#7-registration)
8. [Handling Exceptions in Filters](#8-exceptions)
9. [Filters in Spring Security](#9-security-filters)
10. [Performance Impact of Many Filters](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Servlet Filter?
A Filter is an object that performs filtering tasks on either the request to a resource (a servlet or static content), or on the response from a resource, or both. It operates at the Servlet container level, before the request enters the Spring context.

---

## 2. How the Filter Chain Works
When a request comes in, the container creates a `FilterChain`. Each filter performs its logic and then calls `chain.doFilter(request, response)` to pass the request to the next filter in the sequence. If a filter doesn't call `doFilter`, the request is blocked.

---

## 3. OncePerRequestFilter
Standard `Filter` can sometimes be invoked multiple times for a single request (e.g., during internal forwards). `OncePerRequestFilter` is a Spring-provided base class that guarantees the filter logic is executed exactly once per request.

---

## 4. The Three Methods
- **init**: Called once by the container when the filter is put into service.
- **doFilter**: The core logic. Receives the request, response, and chain.
- **destroy**: Called once by the container before the filter is removed from service.

---

## 5. The Classic Interview Trap: Filter Order
**The Trap**: You have two filters, `LoggingFilter` and `AuthFilter`. You want logging to happen even if auth fails.
**The Problem**: By default, Spring-managed filters have the same priority.
**The Fix**: Use the `@Order` annotation or `FilterRegistrationBean.setOrder()`. Lower numbers have higher priority (run earlier).

---

## 6. Modifying Requests
Because the `HttpServletRequest` is immutable for many properties (like parameters or body), you cannot simply "set" a new value. You must use a `HttpServletRequestWrapper` to override the methods and return your modified data.

---

## 7. Registration
- **@WebFilter**: The Java EE standard. Requires `@ServletComponentScan`.
- **FilterRegistrationBean**: The Spring Boot way. More flexible as it allows you to set the order and URL patterns programmatically.

---

## 8. Handling Exceptions
**CRITICAL**: If an exception occurs in a Filter, it will **NOT** be caught by a `@ControllerAdvice`. You must use a try-catch block inside the filter and manually write the error response, or re-route the request to an error controller.

---

## 9. Spring Security Filters
Spring Security is just a big chain of filters (`SecurityFilterChain`) that is plugged into the main Servlet filter chain using a `DelegatingFilterProxy`.

---

## 10. Performance
Filters are extremely fast, but adding dozens of them adds overhead. For high-performance apps, avoid doing heavy I/O or database calls inside a filter.

---

## 11. Common Mistakes
1. Forgetting to call `chain.doFilter()` (freezes the request).
2. Assuming `OncePerRequestFilter` is always better than `Filter` (usually it is, but understand the "why").
3. Trying to access Spring Beans in a filter registered with `@WebFilter` (use `FilterRegistrationBean` for better integration).

---

## 12. Quick-Fire Interview Q&A
**Q: Can a filter change the destination of a request?**  
A: Yes, by using `request.getRequestDispatcher("...").forward()`.  
**Q: What is the main use case for Filters?**  
A: Logging, GZIP compression, Authentication, and CORS.
