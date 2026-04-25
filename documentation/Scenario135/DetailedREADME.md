# Circular Dependencies — Complete Interview Reference

## Table of Contents
1. [What is a Circular Dependency?](#1-what-is-circular-dependency)
2. [How Spring Detects Circular Dependencies](#2-detection)
3. [Constructor vs Setter Injection behavior](#3-injection-behavior)
4. [The Role of @Lazy in Fixing the Cycle](#4-lazy-fix)
5. [The Classic Interview Trap: Spring Boot 2.6+ Changes](#5-the-classic-interview-trap-changes)
6. [Architectural Refactoring (The Real Fix)](#6-architectural-fix)
7. [Circular Dependency in Bean Post Processors](#7-post-processor-cycle)
8. [Using ApplicationContextAware as a workaround](#8-workaround)
9. [Circular Dependencies and AOP Proxies](#9-aop-proxies)
10. [Testing for Circular Dependencies](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Circular Dependency?
A circular dependency occurs when Bean A depends on Bean B, and Bean B depends on Bean A. This creates a "chicken and egg" problem where Spring cannot finish initializing either bean.

---

## 2. Detection
During startup, Spring maintains a "currently in creation" set of beans. If it tries to create Bean A, and while doing so, it encounters a request to create Bean A again (via Bean B), it throws a `BeanCurrentlyInCreationException`.

---

## 3. Injection Behavior
- **Constructor Injection**: Spring cannot create the instance of Bean A because the constructor requires Bean B, which requires Bean A. This **always** fails immediately at startup.
- **Setter/Field Injection**: Spring can create an "incomplete" instance of Bean A (using the default constructor), put it in the "early singleton objects" cache, then create Bean B and inject that incomplete Bean A into it. This **might** work, but is considered poor practice.

---

## 4. The @Lazy Fix
By adding `@Lazy` to one of the dependencies, Spring injects a **Proxy** instead of the actual bean. The actual bean is only initialized the first time a method is called on that proxy. This breaks the startup cycle because Spring can finish creating Bean A without immediately needing to create Bean B.

---

## 5. The Classic Interview Trap: allow-circular-references
**The Trap**: A user says, *"My app worked in Spring Boot 2.5 but fails to start in 2.6 with a circular dependency error. Why?"*
**The Answer**: Starting with Spring Boot 2.6, circular dependencies are **forbidden by default**.
**The Fix**: You can re-enable them by setting `spring.main.allow-circular-references=true`, but this is strongly discouraged. You should use `@Lazy` or refactor your code instead.

---

## 6. Architectural Refactoring
The best way to fix a cycle is to break it:
1. **Extract a third bean**: Move the common logic to Bean C that both A and B depend on.
2. **Use Events**: Have Bean A publish an event that Bean B listens to, removing the direct dependency.

---

## 7. Post Processors
If you have a circular dependency involving a bean that needs to be proxied (like a `@Transactional` bean), the logic becomes even more complex because Spring must manage "partially created" proxies.

---

## 8. ApplicationContextAware
As a "last resort", a bean can implement `ApplicationContextAware` and manually fetch its dependency from the context when needed, though this couples your code to the Spring Framework.

---

## 9. AOP Proxies
Circular dependencies are often caused by AOP (like Security or Transactions). If Bean A is a proxy and Bean B depends on it, but the proxy isn't fully ready, you get a `BeanCurrentlyInCreationException`.

---

## 10. Testing
You don't usually test for these; if they exist, your application **fails to start**. The error message in the console is usually very descriptive, showing the path of the cycle: `A -> B -> A`.

---

## 11. Common Mistakes
1. Relying on Setter injection to "hide" circular dependencies.
2. Overusing `@Lazy` instead of fixing the underlying architectural mess.
3. Creating massive "God Classes" that naturally tend to depend on everything else.

---

## 12. Quick-Fire Interview Q&A
**Q: Is Constructor injection better for circular dependency detection?**  
A: Yes, it forces you to face the problem at startup rather than having a partially-initialized bean at runtime.  
**Q: Can I have a cycle between 3 beans?**  
A: Yes, `A -> B -> C -> A` is a perfectly valid (and annoying) circular dependency.
