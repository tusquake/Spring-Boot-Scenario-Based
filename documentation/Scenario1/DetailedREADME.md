# Spring Component Scanning — Complete Interview Reference

## Table of Contents
1. [What is Component Scanning?](#1-what-is-component-scanning)
2. [The @ComponentScan Annotation](#2-the-componentscan-annotation)
3. [Stereotype Annotations](#3-stereotype-annotations)
4. [How Scanning Works Internally](#4-how-scanning-works-internally)
5. [The Classic Interview Trap: The "Missing Bean" Mystery](#5-the-classic-interview-trap-the-missing-bean-mystery)
6. [Fixes for Component Scan Issues](#6-fixes-for-component-scan-issues)
7. [Filtering Scans (Include/Exclude)](#7-filtering-scans)
8. [Performance Impact of Large Scans](#8-performance-impact-of-large-scans)
9. [Component Scan vs @Import](#9-componentscan-vs-import)
10. [Scanning in Multi-Module Projects](#10-scanning-in-multi-module-projects)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Component Scanning?

**Component Scanning** is the mechanism Spring uses to automatically discover and register beans in the IoC container. Instead of manually declaring every `@Bean` in a configuration class, Spring "scans" specified packages for classes annotated with stereotype annotations like `@Component`, `@Service`, `@Repository`, or `@Controller`.

Without this, Spring would not know which classes to manage, and dependency injection (`@Autowired`) would fail because no candidates would exist.

---

## 2. The @ComponentScan Annotation

### Definition
The `@ComponentScan` annotation tells Spring **where** to look for beans. It can be applied to any `@Configuration` class.

```java
@Configuration
@ComponentScan(basePackages = "com.interview.debug")
public class AppConfig { }
```

### The @SpringBootApplication Shortcut
In modern Spring Boot apps, you rarely see `@ComponentScan` explicitly. This is because the `@SpringBootApplication` annotation is a "meta-annotation" that includes:
- `@SpringBootConfiguration`
- `@EnableAutoConfiguration`
- `@ComponentScan` (with no arguments, defaulting to the current package and sub-packages)

---

## 5. The Classic Interview Trap: The "Missing Bean" Mystery

### The Problem
This is a favorite for senior-level interviews. Imagine this package structure:

```text
com.interview.debug
 └── DebugApplication.java (@SpringBootApplication)
 └── controller
      └── MyController.java

com.interview.external.service
 └── OrderService.java (@Service)
```

If `MyController` tries to `@Autowired OrderService`, Spring will throw:
`NoSuchBeanDefinitionException: No qualifying bean of type 'com.interview.external.service.OrderService' available`

### Why?
By default, `@SpringBootApplication` only scans the package it resides in (`com.interview.debug`) and its descendants. The `com.interview.external` package is a "sibling" or outside the root, so Spring never "sees" it.

---

## 6. Fixes for Component Scan Issues

There are three ways to solve the "Missing Bean" problem:

### Fix 1: Explicitly Define basePackages (Most Common)
Update your `@SpringBootApplication` or `@Configuration` class to include the external package:

```java
@SpringBootApplication
@ComponentScan(basePackages = { "com.interview.debug", "com.interview.external.service" })
public class DebugApplication { ... }
```

### Fix 2: Move the Application Class
Move `DebugApplication.java` to a higher-level package (e.g., `com.interview`) so that all services become sub-packages.

### Fix 3: Use @Import
If you only need one or two specific beans from an external package, you can import them directly:

```java
@SpringBootApplication
@Import(OrderService.class)
public class DebugApplication { ... }
```

---

## 11. Common Mistakes

1.  **Wrong Package Structure**: Putting the main application class in a deep sub-package, causing it to miss all sibling or parent packages.
2.  **Forgetting Stereotype Annotations**: Creating a class but forgetting to add `@Component`, `@Service`, etc. Spring won't scan it.
3.  **Conflicting Bean Names**: Having two classes with the same name in different scanned packages. Spring will throw a `ConflictingBeanDefinitionException`.
4.  **Scanning Everything**: Pointing `@ComponentScan` to `com` or `org`. This makes application startup extremely slow as Spring tries to scan thousands of library classes.

---

## 12. Quick-Fire Interview Q&A

**Q: Does Spring scan JAR files?**
A: Yes. If a JAR's package is included in the `basePackages` list, Spring will scan it just like local code.

**Q: What is the difference between @Component and @Service?**
A: Technically, nothing. `@Service` is a specialization of `@Component`. However, `@Service` is used to indicate business logic, and in the future, Spring might add specific behavior to it (like AOP points).

**Q: Can we have multiple @ComponentScan annotations?**
A: Yes, using the `@ComponentScans` (plural) container annotation, or simply by providing an array to `basePackages`.

**Q: Is Component Scanning mandatory?**
A: No. You can manually register all beans using `@Bean` methods in `@Configuration` classes, but it is tedious for large projects.
