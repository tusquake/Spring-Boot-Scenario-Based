# Feature Toggles with @ConditionalOnProperty — Complete Interview Reference

## Table of Contents
1. [What is a Feature Toggle?](#1-what-is-a-feature-toggle)
2. [Why Use Feature Toggles?](#2-why-use-it)
3. [Spring's @ConditionalOnProperty Annotation](#3-conditional-annotation)
4. [Using Optional Injection in Controllers](#4-optional-injection)
5. [The Classic Interview Trap: Runtime vs Startup Toggles](#5-the-classic-interview-trap-runtime-vs-startup)
6. [Toggling Entire Configuration Classes](#6-toggling-configs)
7. [Default Values in Conditions](#7-default-values)
8. [Combining Multiple Conditions](#8-combining-conditions)
9. [Feature Toggles in Microservices (Shared Config)](#9-microservices)
10. [A/B Testing and Canary Releases](#10-ab-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Feature Toggle?
A feature toggle (or feature flag) is a technique that allows you to enable or disable features at runtime (or startup) without deploying new code.

---

## 2. Why Use Feature Toggles?
- **CI/CD**: Merge code to master even if the feature isn't finished (keep it toggled off).
- **Risk Mitigation**: If a new feature causes errors in production, you can "kill" it instantly.
- **Canary Releases**: Enable a feature for only 5% of users.

---

## 3. @ConditionalOnProperty
This is the core Spring annotation for feature toggles. It tells Spring to only create a bean if a specific property in `application.properties` matches a certain value.
```java
@Service
@ConditionalOnProperty(name = "app.beta-enabled", havingValue = "true")
public class BetaService { ... }
```

---

## 4. Optional Injection
If a service is toggled off, its bean won't exist in the Spring context. You must inject it as an `Optional` or use `@Autowired(required = false)` to prevent the application from failing to start.

---

## 5. The Classic Interview Trap: Runtime vs Startup
**The Trap**: `@ConditionalOnProperty` is a **STARTUP** toggle. If you change the property in the file, you MUST restart the application for the change to take effect.
**The Fix**: For **RUNTIME** toggles (changing behavior without restart), you should use a simple `if` check inside your service or use a dedicated tool like **Unleash** or **LaunchDarkly**.

---

## 6. Toggling Entire Configuration Classes
You can apply `@ConditionalOnProperty` at the class level of a `@Configuration` class to enable/disable an entire module (e.g., disable all Swagger/OpenAPI beans in production).

---

## 7. Default Values
Always define `matchIfMissing = true` or `false` to decide what happens if the property is completely missing from the configuration file.

---

## 8. Combining Multiple Conditions
You can use `@ConditionalOnExpression` for more complex logic (e.g., "if property A is true AND property B is false").

---

## 9. Feature Toggles in Microservices
In a distributed system, feature flags are often managed centrally (e.g., Spring Cloud Config or AWS AppConfig) so they can be toggled across all services simultaneously.

---

## 10. A/B Testing
Feature toggles are the foundation of A/B testing, where different versions of a feature are shown to different user segments to measure which one performs better.

---

## 11. Common Mistakes
1. Hardcoding toggle checks throughout the code (leads to "if-else" hell).
2. Forgetting to remove toggles after a feature is 100% launched (technical debt).
3. Not testing the "OFF" state of a feature.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between @Conditional and @Profile?**  
A: `@Profile` is a high-level toggle for environments (Dev/Prod). `@Conditional` is a granular toggle for specific properties/classes.  
**Q: Can I toggle a feature without restarting?**  
A: Not with `@ConditionalOnProperty`. You would need `@RefreshScope` or a dynamic flag check.
