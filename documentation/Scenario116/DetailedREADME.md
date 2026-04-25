# @Order & Ordered Interface — Complete Interview Reference

## Table of Contents
1. [Introduction to Ordering in Spring](#1-introduction)
2. [The @Order Annotation](#2-order-annotation)
3. [The Ordered Interface (getOrder)](#3-ordered-interface)
4. [Priority vs Order](#4-priority-vs-order)
5. [The Classic Interview Trap: @Order on @Component vs @Bean](#5-the-classic-interview-trap-trap)
6. [Injecting a Sorted List of Beans](#6-injecting-list)
7. [Ordering in AOP Aspects](#7-aop-ordering)
8. [Ordering Filter Chains](#8-filter-ordering)
9. [Ordering Configuration Properties Loading](#9-config-ordering)
10. [Lowest vs Highest Precedence](#10-precedence)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
When multiple beans of the same type exist (e.g., multiple Validators or Interceptors), Spring often needs a way to decide the execution sequence. `@Order` is the mechanism that defines this sequence.

---

## 2. The @Order Annotation
A simple annotation that takes an integer value. 
**Crucial Point**: Lower values have **HIGHER** priority. `Ordered.HIGHEST_PRECEDENCE` is `Integer.MIN_VALUE`.

---

## 3. The Ordered Interface
An alternative to the annotation. Your bean implements `Ordered` and provides the `getOrder()` method. This is useful if you want to calculate the priority dynamically at runtime based on environment variables.

---

## 4. Priority vs Order
While `@Order` is a Spring annotation, `@Priority` is a standard JSR-250 annotation. Spring recognizes both and treats them similarly, but `@Priority` is also used for bean selection when multiple candidates exist (autowiring).

---

## 5. The Classic Interview Trap: Injection Order
**The Trap**: You add `@Order(1)` to `BeanA` and `@Order(2)` to `BeanB`. You then inject ONE bean: `@Autowired private MyService service`.
**The Problem**: `@Order` does **NOT** influence which bean is injected for a single-valued dependency. Spring will still throw a `NoUniqueBeanDefinitionException`.
**The Fix**: Use `@Primary` to decide which single bean to inject. `@Order` only influences the order when injecting into a **Collection** (List/Array).

---

## 6. Sorted List Injection
```java
@Autowired
private List<MyService> services; // Spring will sort this list based on @Order
```

---

## 7. Ordering in AOP
If multiple aspects match the same join point, the order of execution is determined by `@Order`. The aspect with the lowest order value runs "first" on the way in and "last" on the way out.

---

## 8. Filter Ordering
For Servlet Filters, you should use `FilterRegistrationBean.setOrder()` because the standard `@Order` annotation on a `@Component` filter bean is sometimes ignored by the servlet container.

---

## 9. Lowest vs Highest Precedence
- `Ordered.LOWEST_PRECEDENCE`: `Integer.MAX_VALUE`. (Runs last).
- `Ordered.HIGHEST_PRECEDENCE`: `Integer.MIN_VALUE`. (Runs first).

---

## 10. Practical Use Case
A common use case is a "Chain of Responsibility" pattern where you want several processors to handle a request in a specific order (e.g., a `SanitizationProcessor` must run before a `ValidationProcessor`).

---

## 11. Common Mistakes
1. Thinking that a higher `@Order` number means higher priority (it's the opposite).
2. Expecting `@Order` to solve `NoUniqueBeanDefinitionException` for single injections.
3. Placing `@Order` on a `@Configuration` class and expecting it to order the `@Bean` methods inside (it only orders the configuration classes themselves).

---

## 12. Quick-Fire Interview Q&A
**Q: Does @Order work for @Bean methods?**  
A: Yes, but only when you are injecting a collection of those beans.  
**Q: What is the default order if the annotation is missing?**  
A: `Ordered.LOWEST_PRECEDENCE` (it runs last).
