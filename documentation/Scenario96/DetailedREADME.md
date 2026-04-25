# Startup Validation & @ConfigurationProperties — Complete Interview Reference

## Table of Contents
1. [Introduction to Startup Validation](#1-introduction)
2. [Why Validate at Startup? (Fail-Fast)](#2-why-validate)
3. [Using @Validated on Configuration Classes](#3-using-validated)
4. [Standard JSR-303 Constraints (@NotNull, @Min)](#4-standard-constraints)
5. [The Classic Interview Trap: Validating Nested Properties](#5-the-classic-interview-trap-nested)
6. [Relaxed Binding in Spring Boot](#6-relaxed-binding)
7. [Custom Validator Implementation](#7-custom-validators)
8. [Property Source Loading Order](#8-loading-order)
9. [Handling Configuration Failures gracefully](#9-handling-failures)
10. [Testing Configuration Properties](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
Startup validation ensures that your application has all the configuration it needs to run correctly before it starts accepting traffic. If a critical property (like a DB password or API key) is missing or invalid, the app should fail to start.

---

## 2. Why Fail-Fast?
It is much better to have a deployment fail immediately in the CI/CD pipeline than to have a running application that crashes 2 hours later when it finally tries to use an invalid configuration value.

---

## 3. Using @Validated
By adding `@Validated` to a class marked with `@ConfigurationProperties`, Spring will automatically run the Bean Validation engine against the properties after they are bound but before the context is fully started.

---

## 4. Standard Constraints
You can use all standard annotations:
- `@NotBlank`: Ensure string is not null or empty.
- `@Min(100)` / `@Max(500)`: Ensure numeric ranges.
- `@Email`: Ensure correct email format for admin contact fields.
- `@Pattern`: Use regex for complex values like API keys.

---

## 5. The Classic Interview Trap: Nested Validation
**The Trap**: You have a nested class inside your config (e.g., `SecuritySettings.Encryption`). You add `@NotBlank` to a field in the nested class.
**The Problem**: Validation will **NOT** run for the nested class by default.
**The Fix**: You must add the `@Valid` annotation to the nested field in the parent class to trigger "Cascaded Validation".

---

## 6. Relaxed Binding
Spring Boot is very flexible with property names. All of the following will bind to a field named `firstName`:
- `first-name` (Kebab-case, recommended for .yml)
- `first_name` (Snake-case)
- `FIRSTNAME` (Upper-case, common for Env Vars)

---

## 7. Custom Validators
If standard annotations aren't enough (e.g., you need to check if a URL is actually reachable), you can implement the `org.springframework.validation.Validator` interface and register it with your configuration class.

---

## 8. Loading Order
Remember that properties are loaded from many sources in a specific order:
1. Command line arguments
2. Environment variables
3. Config-specific profiles (`application-prod.yml`)
4. Default properties (`application.yml`)

---

## 9. Configuration Failure
If validation fails, Spring throws a `ConfigurationPropertiesBindException`. The logs will clearly show which property failed and what the constraint violation was.

---

## 10. Testing
Use `@EnableConfigurationProperties` in your test to verify that your binding and validation logic works correctly without starting the entire application.

---

## 11. Common Mistakes
1. Forgetting `@Validated` on the class level.
2. Forgetting `@Valid` on nested objects.
3. Hardcoding defaults instead of letting validation catch missing required fields.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @Value with @Validated?**  
A: No. `@Value` properties are not validated by the Bean Validation engine in the same way.  
**Q: What is the benefit of @ConfigurationProperties over @Value?**  
A: It supports hierarchical structures, relaxed binding, and automatic startup validation.
