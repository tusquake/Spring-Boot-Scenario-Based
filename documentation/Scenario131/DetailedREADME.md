# Dynamic JSON Filtering — Complete Interview Reference

## Table of Contents
1. [What is JSON Filtering?](#1-introduction)
2. [Static vs Dynamic Filtering](#2-static-vs-dynamic)
3. [The @JsonFilter Annotation](#3-json-filter-annotation)
4. [Using SimpleBeanPropertyFilter](#4-simple-property-filter)
5. [The Role of MappingJacksonValue](#5-mapping-jackson-value)
6. [The Classic Interview Trap: One Filter per Object](#6-the-classic-interview-trap-trap)
7. [Filtering based on User Roles (RBAC)](#7-role-based-filtering)
8. [Filtering for Different API Versions](#8-version-filtering)
9. [Performance of Dynamic Filtering](#9-performance)
10. [Alternatives: @JsonView](#10-alternatives)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is JSON Filtering?
JSON filtering allows you to control which fields of a Java object are serialized into the JSON response. This is crucial for security (hiding passwords) and performance (returning smaller payloads).

---

## 2. Static vs Dynamic
- **Static**: `@JsonIgnore` or `@JsonProperty(access = READ_ONLY)`. The decision is hardcoded in the class.
- **Dynamic**: The decision is made at runtime in the Controller. For example, a "User" sees fewer fields than an "Admin" for the same object.

---

## 3. @JsonFilter
To use dynamic filtering, you must first mark your class with a filter ID:
`@JsonFilter("UserFilter")`
`public class UserDto { ... }`

---

## 4. SimpleBeanPropertyFilter
This is the utility class provided by Jackson to define filtering rules:
- `filterOutAllExcept("id", "name")`: Only these fields will be serialized.
- `serializeAllExcept("password")`: Everything but password will be serialized.

---

## 5. MappingJacksonValue
In your `@RestController` method, you return `MappingJacksonValue` instead of the DTO directly. This wrapper holds both the data and the filter logic.

---

## 6. The Classic Interview Trap: The Missing Filter Error
**The Trap**: You add `@JsonFilter` to a class but forget to provide a filter in one of your controllers.
**The Problem**: Jackson will throw a `JsonMappingException`: "Can not resolve PropertyFilter with id 'UserFilter'". Your entire API endpoint will crash with a 500 error.
**The Fix**: If a class has `@JsonFilter`, you **MUST** provide a filter provider in the controller, even if it's a "pass-through" filter that serializes everything.

---

## 7. Role-Based Filtering
Dynamic filtering is the best way to implement "Field-level security".
- `/api/profile/me` -> Filter returns all fields.
- `/api/profile/public/123` -> Filter returns only name and avatar.

---

## 8. API Versioning
If v2 of your API removes a field, you can use a dynamic filter to hide that field for v2 requests while keeping it visible for v1 requests, using the same Java model.

---

## 9. Performance
Dynamic filtering is slightly slower than static ignoring because it involves map lookups and extra object wrapping. However, for most business applications, the flexibility far outweighs the tiny performance cost.

---

## 10. Alternatives: @JsonView
`@JsonView` is another way to do dynamic filtering. It is more "declarative" (using interfaces as views) but less flexible than the programmatic approach of `@JsonFilter`.

---

## 11. Common Mistakes
1. Forgetting to register the filter ID in the DTO.
2. Typos in field names inside `filterOutAllExcept`. (If you misspell "username" as "user_name", the field will be silently ignored).
3. Hardcoding filter logic in the Service layer (Filters should stay in the Controller/Web layer).

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @JsonFilter on a List?**  
A: Yes, you just wrap the `List` in the `MappingJacksonValue`.  
**Q: What happens if I use both @JsonIgnore and @JsonFilter?**  
A: `@JsonIgnore` takes precedence. Even if the filter says "include this field", Jackson will skip it if it's ignored statically.
