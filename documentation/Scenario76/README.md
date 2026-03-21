# Scenario 76: @Value vs @ConfigurationProperties

## Overview
Spring Boot provides two main ways to inject external properties into your beans: `@Value` and `@ConfigurationProperties`. For senior roles, it's crucial to know why `@ConfigurationProperties` is usually the superior choice for complex configurations.

## Comparison Table 📊

| Feature | `@Value` | `@ConfigurationProperties` |
| :--- | :--- | :--- |
| **Type Safety** | ❌ Limited (needs explicit casting/mapping) | ✅ Fully Type-Safe (Standard POJO) |
| **Relaxed Binding** | ❌ No (Exact name match only) | ✅ Yes (`token-timeout` maps to `tokenTimeout`) |
| **Validation** | ❌ No built-in support | ✅ Supports JSR-303/380 Bean Validation |
| **Hierarchical Structure**| ❌ Difficult to maintain | ✅ Excellent (Nested static classes) |
| **SpEL Support** | ✅ Yes (`@Value("#{...}")`) | ❌ No |

## Key Concepts 💡

### 1. Relaxed Binding
Spring Boot's "relaxed binding" allows you to define properties in any standard format (`kebab-case`, `snake_case`, `camelCase`) and map them correctly to Java camelCase variables.
- Property: `app.security.token-timeout`
- Java: `private int tokenTimeout;` // OK!

### 2. Startup Validation
By adding `@Validated` to your `@ConfigurationProperties` bean, you can ensure that the application **fails to start** if the properties are invalid (e.g., a negative timeout or a missing required key). This is far better than discovering a configuration error at runtime.

### 3. Separation of Concerns
With `@ConfigurationProperties`, configurations are grouped into their own classes rather than cluttering your controllers or services with many `@Value` fields.

## Interview Tips 💡
- **When to use `@Value`?** Use it for single, one-off properties, or when you specifically need the power of SpEL (Spring Expression Language).
- **Why use `@ConfigurationProperties` for third-party libraries?** It allows you to bind entire configuration sections from YAML/Properties files directly to the library's config beans.
- **Can they be used together?** Yes, but it's generally better to be consistent.

## Testing the Scenario
1. **Check Values**: `GET /api/scenario76/compare`
   - Observe how `app.security.token-timeout` (kebab-case) and `app.security.max_retries` (snake_case) both mapped correctly to the Java bean.
2. **Test Validation**: Change `app.security.token-timeout` to `-1` in `application.properties` and restart the app. It will fail to start with a `BindValidationException`.
