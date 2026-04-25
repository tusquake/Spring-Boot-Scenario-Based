# Type-Safe Configuration Properties — Complete Interview Reference

## Table of Contents
1. [What is Type-Safe Configuration?](#1-what-is-type-safe)
2. [@Value vs @ConfigurationProperties (Recap)](#2-value-vs-config)
3. [Binding Lists and Maps](#3-lists-and-maps)
4. [Nested Configuration Objects](#4-nested-objects)
5. [The Classic Interview Trap: Immutable Configuration](#5-the-classic-interview-trap-immutable)
6. [Relaxed Binding Strategies](#6-relaxed-binding)
7. [Validation with JSR-303 (@Validated)](#7-validation)
8. [Property Conversion (Duration, DataSize)](#8-conversion)
9. [Generating Configuration Metadata](#9-metadata)
10. [Configuration Refresh (@RefreshScope)](#10-refresh)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Type-Safe Configuration?
Type-safe configuration allows you to map groups of external properties (from `application.properties` or environment variables) into Java objects. This provides IDE support, validation, and cleaner code compared to using individual `@Value` annotations.

---

## 2. @Value vs @ConfigurationProperties
- **@Value**: Good for a single secret or simple flag.
- **@ConfigurationProperties**: Best for structured data (e.g., database settings, mail server config, feature flags).

---

## 3. Binding Lists and Maps
You can easily bind comma-separated lists or key-value pairs from properties:
```properties
app.support-emails=a@test.com,b@test.com
app.feature-flags.ui-enabled=true
```
Spring maps these directly to `List<String>` and `Map<String, Boolean>`.

---

## 4. Nested Configuration Objects
You can create a hierarchy of objects. For example, an `AppConfig` class can have a nested `SecurityConfig` object, matching the structure of your YAML/Properties file.

---

## 5. The Classic Interview Trap: Immutable Configuration
**The Question**: *"Can we make @ConfigurationProperties objects immutable?"*
**The Answer**: **YES**. Since Spring Boot 2.2, you can use `@ConstructorBinding`. This allows you to use `final` fields and no setters, making your configuration safer and more robust.

---

## 6. Relaxed Binding
Spring is smart about property names. `app.max-retries`, `app.maxRetries`, and `APP_MAX_RETRIES` will all bind to the Java field `maxRetries`.

---

## 7. Validation with JSR-303
Add `@Validated` to your config class and use annotations like `@Min(1)` or `@NotBlank` to ensure your app doesn't start with invalid settings.

---

## 8. Property Conversion
Spring automatically converts strings to complex types:
- `10s` -> `Duration`
- `50MB` -> `DataSize`
- `2023-10-27` -> `LocalDate`

---

## 9. Generating Configuration Metadata
Include the `spring-boot-configuration-processor` in your `pom.xml`. It generates a JSON file that enables autocomplete and documentation for your custom properties in IDEs like IntelliJ.

---

## 10. Configuration Refresh
In a cloud environment, you might want to change properties without restarting. Using `@RefreshScope` (from Spring Cloud) allows your beans to be re-initialized with new property values on the fly.

---

## 11. Common Mistakes
1. Not adding `@ConfigurationPropertiesScan` or `@EnableConfigurationProperties`.
2. Forgetting that fields must have setters (unless using `@ConstructorBinding`).
3. Using `@Value` for structured groups of properties.

---

## 12. Quick-Fire Interview Q&A
**Q: Does @ConfigurationProperties work with YAML?**  
A: Yes, it is the preferred way to work with hierarchical YAML files.  
**Q: Can I use @ConfigurationProperties on a @Bean method?**  
A: Yes, this is common when configuring third-party beans (like a `DataSource`).
