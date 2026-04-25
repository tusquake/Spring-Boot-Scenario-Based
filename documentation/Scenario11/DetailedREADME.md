# Spring Validation Groups — Complete Interview Reference

## Table of Contents
1. [What are Validation Groups?](#1-what-are-validation-groups)
2. [@Valid vs @Validated](#2-valid-vs-validated)
3. [Creating Group Interfaces](#3-creating-group-interfaces)
4. [Applying Groups to Model Fields](#4-applying-groups)
5. [The Classic Interview Trap: The Default Group](#5-the-classic-interview-trap-default-group)
6. [Using Groups in @RestController](#6-using-groups-in-controller)
7. [Group Inheritance and Sequence](#7-group-inheritance)
8. [Conditional Validation Logic](#8-conditional-validation)
9. [Payloads in Validation](#9-payloads)
10. [Validation at the Service Layer](#10-service-layer-validation)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are Validation Groups?
Validation Groups allow you to apply different validation rules to the same model depending on the context (e.g., `password` is required on **Create** but optional on **Update**).

---

## 2. @Valid vs @Validated
- **@Valid**: Standard JSR-303/JSR-380 annotation. Does NOT support groups.
- **@Validated**: Spring-specific annotation. Supports groups and can be used at the class level.

---

## 3. Creating Group Interfaces
Groups are just plain Java interfaces used as markers.
```java
public interface OnCreate {}
public interface OnUpdate {}
```

---

## 4. Applying Groups to Model Fields
You specify which group a constraint belongs to using the `groups` attribute.
```java
@NotBlank(groups = OnCreate.class)
private String password;
```

---

## 5. The Classic Interview Trap: The Default Group
**The Trap**: If you specify a group on a controller (e.g., `@Validated(OnCreate.class)`), Spring will **ONLY** validate fields marked with `OnCreate`. Fields with no group (which belong to the `Default` group) will be ignored.
**The Fix**: Include `Default.class` in the `@Validated` annotation: `@Validated({OnCreate.class, Default.class})`.

---

## 6. Using Groups in @RestController
Apply `@Validated` to the `@RequestBody` in your controller methods.

---

## 7. Group Inheritance and Sequence
You can define a sequence of groups using `@GroupSequence` to ensure that basic validations (like `@NotNull`) run before expensive ones (like custom DB checks).

---

## 8. Conditional Validation Logic
While groups handle static scenarios (Create/Update), for dynamic logic (e.g., "if type is X, then Y is required"), you usually need a custom Class-level validator.

---

## 9. Payloads in Validation
Payloads allow you to attach metadata to a constraint, which can be used by the validation engine for custom error handling or logging.

---

## 10. Validation at the Service Layer
By putting `@Validated` on a Service class, Spring will validate the parameters of every method call, acting as a "fail-fast" mechanism.

---

## 11. Common Mistakes
1. Using `@Valid` instead of `@Validated` when groups are needed.
2. Forgetting to include the `Default.class` group.
3. Defining groups as classes instead of interfaces.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a field belong to multiple groups?**  
A: Yes, e.g., `groups = {OnCreate.class, OnUpdate.class}`.  
**Q: What is the benefit of Validation Groups?**  
A: It prevents code duplication by allowing one DTO to be used for multiple API actions with different rules.
