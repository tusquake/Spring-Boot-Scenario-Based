# Polymorphic JSON Mapping — Complete Interview Reference

## Table of Contents
1. [What is Polymorphic JSON?](#1-definition)
2. [Why Use It? (Heterogeneous Lists)](#2-why-use-it)
3. [The @JsonTypeInfo Annotation](#3-json-type-info)
4. [The @JsonSubTypes Annotation](#4-json-sub-types)
5. [The Classic Interview Trap: Missing the 'type' field](#5-the-classic-interview-trap-trap)
6. [Type IDs: Class Name vs Logical Name](#6-type-ids)
7. [Using @JsonTypeName for Clean Mapping](#7-type-names)
8. [Deserializing Interfaces vs Abstract Classes](#8-deserialization)
9. [Handling Default Subclasses](#9-default-subclass)
10. [Polymorphism in Spring Data REST](#10-spring-data-rest)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Polymorphic JSON?
Polymorphic JSON mapping allows Jackson to correctly handle a hierarchy of classes. When you send a list of `Vehicle` objects, Jackson can determine whether an individual JSON object should be instantiated as a `Car` or a `Bike` based on a "discriminator" field in the JSON.

---

## 2. Why Use It?
In a REST API, you might have a single endpoint that accepts multiple types of input (e.g., a "Payment" can be a "CreditCardPayment" or a "PaypalPayment"). Without polymorphism, you would have to write complex custom deserialization logic.

---

## 3. @JsonTypeInfo
This annotation defines **HOW** the type information is stored in the JSON.
- `use = Id.NAME`: Use a logical name (e.g., "car").
- `include = As.PROPERTY`: Store the name in a JSON property.
- `property = "type"`: The name of the property to look for.

---

## 4. @JsonSubTypes
This annotation defines **WHICH** subclasses are available and what their names are:
```java
@JsonSubTypes({
    @JsonSubTypes.Type(value = Car.class, name = "car"),
    @JsonSubTypes.Type(value = Bike.class, name = "bike")
})
```

---

## 5. The Classic Interview Trap: Security Risks
**The Trap**: A developer uses `use = Id.CLASS` for polymorphism.
**The Problem**: This stores the full Java class name (e.g., `com.app.model.AdminUser`) in the JSON. An attacker could potentially send a different class name (like a malicious Gadget class) in the JSON, leading to a **Remote Code Execution (RCE)** vulnerability during deserialization.
**The Fix**: **ALWAYS** use logical names (`Id.NAME`) and explicitly list the allowed subclasses in `@JsonSubTypes`.

---

## 6. Type IDs
- **Id.CLASS**: Full Java class name. (Dangerous and leaks internals).
- **Id.NAME**: A short, logical name. (Safe and clean).

---

## 7. @JsonTypeName
Instead of defining names in the parent class's `@JsonSubTypes`, you can add `@JsonTypeName("car")` directly to the `Car` class.

---

## 8. Interfaces
Polymorphism works exactly the same way for Interfaces. You place the annotations on the interface, and Jackson will return the correct implementation class.

---

## 9. Default Subclass
You can specify a `defaultImpl` in `@JsonTypeInfo`. If Jackson encounters a JSON object with an unknown or missing type field, it will fall back to this class instead of throwing an error.

---

## 10. Spring Integration
Spring Boot's `ObjectMapper` is fully compatible with these annotations. No extra configuration is needed other than adding the annotations to your model classes.

---

## 11. Common Mistakes
1. Forgetting to include the "type" property in the JSON payload (results in `InvalidTypeIdException`).
2. Not defining all possible subclasses in `@JsonSubTypes`.
3. Using `Id.CLASS` in a public-facing API.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use a custom field name other than "type"?**  
A: Yes, you can set `property = "category"` or anything else in `@JsonTypeInfo`.  
**Q: Does polymorphic mapping affect performance?**  
A: Yes, slightly, because Jackson has to look up the type name in a map and perform a bit more logic during deserialization.
