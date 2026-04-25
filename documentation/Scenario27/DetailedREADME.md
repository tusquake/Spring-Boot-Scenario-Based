# Custom AOP Annotations — Complete Interview Reference

## Table of Contents
1. [Introduction to AOP Concepts](#1-introduction-to-aop)
2. [Why Use Custom Annotations?](#2-why-custom-annotations)
3. [Creating a Custom Annotation (@interface)](#3-creating-annotation)
4. [Writing the Aspect (@Aspect, @Around)](#4-writing-aspect)
5. [The Classic Interview Trap: Internal Method Calls](#5-the-classic-interview-trap-internal-calls)
6. [Pointcut Expressions (execution vs @annotation)](#6-pointcut-expressions)
7. [Advice Types (Before, After, Around)](#7-advice-types)
8. [Accessing Annotation Values at Runtime](#8-accessing-values)
9. [AOP Proxies: JDK Dynamic vs CGLIB](#9-aop-proxies)
10. [Handling Exceptions in AOP (@AfterThrowing)](#10-exception-handling)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to AOP Concepts
Aspect-Oriented Programming (AOP) allows you to modularize "cross-cutting concerns" like logging, security, and performance tracking. 
- **Aspect**: The class containing the logic.
- **Advice**: The specific action taken (When to run).
- **Pointcut**: Which methods to target (Where to run).

---

## 2. Why Use Custom Annotations?
Instead of using complex regex-based pointcuts, custom annotations provide a clean, declarative way to apply logic to specific methods.
```java
@TrackTime // Descriptive and easy to use
public void myMethod() { ... }
```

---

## 3. Creating a Custom Annotation
Use `@Retention(RetentionPolicy.RUNTIME)` to ensure the annotation is available at runtime for the AOP engine to see.
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTime {
    String value() default "";
}
```

---

## 4. Writing the Aspect
Use the `@Around` advice to wrap the method execution, allowing you to run code before and after it.
```java
@Around("@annotation(trackTime)")
public Object logTime(ProceedingJoinPoint joinPoint, TrackTime trackTime) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed(); // Call the actual method
    long end = System.currentTimeMillis();
    System.out.println(trackTime.value() + " took " + (end - start) + "ms");
    return result;
}
```

---

## 5. The Classic Interview Trap: Internal Method Calls
**The Trap**: Method A and Method B are in the same class. Method A is annotated with `@TrackTime`. If you call Method B, and Method B calls Method A internally (`this.methodA()`), the AOP logic **will NOT run**.
**Why?**: AOP works via **Proxies**. When you call a method internally, you are bypassing the proxy and calling the raw object directly.

---

## 6. Pointcut Expressions
- `execution(* com.service.*.*(..))`: Matches all methods in a package.
- `@annotation(com.interview.TrackTime)`: Matches only methods with a specific annotation.

---

## 7. Advice Types
- **@Before**: Runs before the method.
- **@AfterReturning**: Runs only if the method completes successfully.
- **@AfterThrowing**: Runs only if an exception is thrown.
- **@Around**: Most powerful; can change arguments and return values.

---

## 8. Accessing Annotation Values
You can pass the annotation as a parameter to your advice method to access its attributes (like `trackTime.value()`).

---

## 9. AOP Proxies
- **JDK Dynamic Proxy**: Used if the class implements an interface.
- **CGLIB Proxy**: Used if the class does NOT implement an interface (subclasses the original class).

---

## 10. Handling Exceptions in AOP
Always ensure your `@Around` advice re-throws exceptions after logging them, or the caller might think the method succeeded when it actually failed.

---

## 11. Common Mistakes
1. Forgetting to mark the aspect class with `@Component` and `@Aspect`.
2. Trying to apply AOP to `private` or `static` methods (they cannot be proxied).
3. Ignoring the performance overhead of complex AOP logic on high-frequency methods.

---

## 12. Quick-Fire Interview Q&A
**Q: Can we apply AOP to a class?**  
A: Yes, by using `@Target(ElementType.TYPE)` on the annotation.  
**Q: What is JoinPoint?**  
A: An object that contains metadata about the current method execution (name, arguments, target object).
