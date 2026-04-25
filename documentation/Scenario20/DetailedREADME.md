# @Value vs @ConfigurationProperties — Complete Interview Reference

## Table of Contents
1. [Introduction to Externalized Configuration](#1-introduction)
2. [Using @Value for Ad-hoc Properties](#2-using-value)
3. [Using @ConfigurationProperties for Structured Data](#3-using-config-props)
4. [Comparison Table: @Value vs @ConfigurationProperties](#4-comparison)
5. [The Classic Interview Trap: SpEL vs Property Placeholders](#5-the-classic-interview-trap-spel)
6. [Relaxed Binding (Hyphens vs CamelCase)](#6-relaxed-binding)
7. [Validation in @ConfigurationProperties (@Validated)](#7-validation)
8. [Property Conversion (Duration, DataSize)](#8-conversion)
9. [Loading External Property Files (@PropertySource)](#9-property-source)
10. [IDE Support and Metadata Generation](#10-ide-support)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to Externalized Configuration
Spring Boot allows you to externalize your configuration so that you can work with the same application code in different environments. You can use properties files, YAML files, environment variables, or command-line arguments.

---

## 2. Using @Value for Ad-hoc Properties
`@Value` is used to inject a single property value into a field.
```java
@Value("${app.name:DefaultName}")
private String appName;
```
It is great for simple, non-hierarchical values.

---

## 3. Using @ConfigurationProperties for Structured Data
`@ConfigurationProperties` maps a whole group of properties to a POJO.
```java
@ConfigurationProperties(prefix = "server")
public class ServerConfig {
    private String host;
    private int port;
}
```
It is the recommended way for large, structured configurations.

---

## 4. Comparison Table
| Feature | @Value | @ConfigurationProperties |
|---|---|---|
| **Relaxed Binding** | No | Yes |
| **SpEL Support** | Yes | No |
| **Validation** | No | Yes (JSR-303) |
| **Type Safety** | Low | High |

---

## 5. The Classic Interview Trap: SpEL vs Property Placeholders
**The Trap**: Mixing up `${...}` and `#{...}`.
- `${app.version}`: Fetches a property from the environment.
- `#{systemProperties['user.dir']}`: Executes a Spring Expression Language (SpEL) expression.
`@ConfigurationProperties` ONLY supports `${...}`.

---

## 6. Relaxed Binding
`@ConfigurationProperties` is "relaxed." It can map `server.port`, `server_port`, or `SERVER_PORT` all to the Java field `port`. `@Value` is strict and requires an exact match.

---

## 7. Validation in @ConfigurationProperties
You can use standard JSR-303 annotations (like `@Min`, `@NotNull`) on your config POJO to ensure the properties are valid at startup.

---

## 8. Property Conversion (Duration, DataSize)
Spring Boot can automatically convert strings like `10s` to a `java.time.Duration` or `10MB` to `org.springframework.util.Unit` when using `@ConfigurationProperties`.

---

## 9. Loading External Property Files
Use `@PropertySource("classpath:custom.properties")` if your properties are not in the standard `application.properties`.

---

## 10. IDE Support and Metadata Generation
By adding the `spring-boot-configuration-processor` dependency, Spring generates a JSON metadata file that gives you autocomplete support in IntelliJ/Eclipse for your custom properties.

---

## 11. Common Mistakes
1. Using `@Value` for a class with 50 properties (too much boilerplate).
2. Forgetting to add `@EnableConfigurationProperties` or `@Configuration` to the POJO.
3. Not handling default values in `@Value`.

---

## 12. Quick-Fire Interview Q&A
**Q: Which one supports SpEL?**  
A: `@Value`.  
**Q: Why use @ConfigurationProperties over @Value?**  
A: It provides type safety, group binding, relaxed binding, and validation.
