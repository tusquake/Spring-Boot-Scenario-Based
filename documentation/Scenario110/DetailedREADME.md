# Jackson JSON Mapping — Complete Interview Reference

## Table of Contents
1. [What is Jackson?](#1-what-is-jackson)
2. [How Spring Boot Uses Jackson](#2-spring-jackson)
3. [Renaming Fields (@JsonProperty)](#3-json-property)
4. [Ignoring Fields (@JsonIgnore, @JsonIgnoreProperties)](#4-json-ignore)
5. [Handling Nulls (@JsonInclude)](#5-json-include)
6. [The Classic Interview Trap: Date Formatting (@JsonFormat)](#6-the-classic-interview-trap-dates)
7. [Custom Serializers and Deserializers](#7-custom-serializers)
8. [Snake Case vs Camel Case (PropertyNamingStrategy)](#8-naming-strategy)
9. [Polymorphic Type Handling (@JsonTypeInfo)](#9-polymorphism)
10. [Mixing in Annotations (@JsonMixIn)](#10-mixins)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Jackson?
Jackson is the default JSON processing library for Java. It is high-performance, flexible, and handles the conversion between Java POJOs and JSON strings (and vice-versa).

---

## 2. Spring Integration
Spring Boot automatically configures an `ObjectMapper` bean. When you return an object from a `@RestController`, Spring uses this `ObjectMapper` to write the response body.

---

## 3. Renaming Fields (@JsonProperty)
Use this to map a Java camelCase field to a specific JSON key name (often snake_case):
`@JsonProperty("user_full_name")`
`private String fullName;`

---

## 4. Ignoring Fields
- **@JsonIgnore**: Applied on a field to prevent it from being serialized (e.g., passwords, secrets).
- **@JsonIgnoreProperties**: Applied at the class level to ignore multiple fields or unknown fields.

---

## 5. Handling Nulls (@JsonInclude)
To keep your JSON clean, you can prevent null fields from appearing in the output:
`@JsonInclude(JsonInclude.Include.NON_NULL)`

---

## 6. The Classic Interview Trap: Dates
**The Trap**: You return a `LocalDateTime`. In the JSON, it looks like a long array of numbers: `[2023, 10, 25, 14, 30]`.
**The Problem**: By default, Jackson serializes Java 8 dates as numeric arrays or timestamps.
**The Fix**: Use `@JsonFormat` to specify the pattern:
`@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")`
`private LocalDateTime createdAt;`

---

## 7. Custom Serializers
If you have a very complex object (like a legacy Money class) that doesn't map easily, you can write a `JsonSerializer<T>` and attach it using `@JsonSerialize`.

---

## 8. Naming Strategies
Instead of adding `@JsonProperty` to every field, you can set a global naming strategy in `application.properties`:
`spring.jackson.property-naming-strategy=SNAKE_CASE`

---

## 9. Polymorphic Handling
If you have a `List<Animal>`, and the list contains `Dog` and `Cat`, Jackson needs to know which subclass to instantiate when reading the JSON back. Use `@JsonTypeInfo` to add a "type" field to the JSON.

---

## 10. Mix-In Annotations
If you are using a 3rd-party library class that you cannot modify, you can create a "Mix-In" class with the Jackson annotations and tell the `ObjectMapper` to apply them to the library class.

---

## 11. Common Mistakes
1. Forgetting that Jackson requires a **No-Args Constructor** for deserialization.
2. Circular references (Parent has Child, Child has Parent) leading to `StackOverflowError`. (Use `@JsonManagedReference` and `@JsonBackReference`).
3. Using `java.util.Date` instead of `java.time` classes.

---

## 12. Quick-Fire Interview Q&A
**Q: How do I ignore unknown fields globally?**  
A: `spring.jackson.deserialization.fail-on-unknown-properties=false`.  
**Q: Is Jackson thread-safe?**  
A: The `ObjectMapper` is thread-safe after configuration, so you should reuse a single instance.
