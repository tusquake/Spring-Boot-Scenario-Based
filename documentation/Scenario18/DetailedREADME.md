# Custom Spring Boot Starters — Complete Interview Reference

## Table of Contents
1. [What is a Spring Boot Starter?](#1-what-is-a-starter)
2. [Components of a Starter](#2-components)
3. [@AutoConfiguration & imports file](#3-autoconfiguration)
4. [Conditional Annotations (@ConditionalOn...)](#4-conditional-annotations)
5. [The Classic Interview Trap: Circular Dependencies in Starters](#5-the-classic-interview-trap-circular)
6. [Starter vs Library](#6-starter-vs-library)
7. [Creating Type-Safe Properties (@ConfigurationProperties)](#7-type-safe-properties)
8. [Ordering Auto-Configuration (@AutoConfigureAfter)](#8-ordering)
9. [Excluding Auto-Configuration](#9-excluding)
10. [Testing Custom Starters](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Spring Boot Starter?
A "Starter" is a set of convenient dependency descriptors that you can include in your application. It bootstraps a specific technology (like Redis, MongoDB, or your own custom logic) with zero or minimal configuration.

---

## 2. Components of a Starter
- **Library Module**: Contains the actual business logic.
- **Auto-Configuration Module**: Contains the `@Configuration` classes that define the beans.

---

## 3. @AutoConfiguration & imports file
Since Spring Boot 2.7/3.0, auto-configuration classes are registered in:
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
Previously, this was done in `spring.factories`.

---

## 4. Conditional Annotations (@ConditionalOn...)
The magic of starters lies in these annotations:
- `@ConditionalOnProperty`: Load bean only if a property is set.
- `@ConditionalOnClass`: Load only if a specific library is in the classpath.
- `@ConditionalOnMissingBean`: Load only if the user hasn't defined their own version of the bean.

---

## 5. The Classic Interview Trap: Circular Dependencies in Starters
**The Trap**: Your starter depends on `Module A`, but `Module A` depends on your starter to work.
**The Fix**: Keep starters thin. They should only contain configuration and minimal logic. Move core logic to a separate "library" module that doesn't know about Spring.

---

## 6. Starter vs Library
- **Library**: Just code. You must manually define the beans in your app.
- **Starter**: Code + Auto-Configuration. Just add the dependency, and it "just works."

---

## 7. Creating Type-Safe Properties
Always use `@ConfigurationProperties` to allow users to configure your starter via `application.properties` with IDE autocomplete support.

---

## 8. Ordering Auto-Configuration
Use `@AutoConfigureAfter(DataSourceAutoConfiguration.class)` to ensure your starter runs *after* the database is ready if it depends on a `DataSource`.

---

## 9. Excluding Auto-Configuration
If a starter is causing issues, a user can exclude it:
`@SpringBootApplication(exclude = {MyCustomAutoConfiguration.class})`.

---

## 10. Testing Custom Starters
Use `ApplicationContextRunner` provided by Spring Boot. It allows you to simulate a Spring context and verify if your beans are created under different conditions/properties.

---

## 11. Common Mistakes
1. Not using `@ConditionalOnMissingBean` (preventing users from overriding your beans).
2. Forgetting to register the auto-configuration class in the `imports` file.
3. Adding too many dependencies to the starter, making it "heavy."

---

## 12. Quick-Fire Interview Q&A
**Q: What is the purpose of spring.factories?**  
A: Historically, it was used to register auto-configurations. In modern Spring Boot, it is replaced by the `.imports` file.  
**Q: Why use @ConditionalOnMissingBean?**  
A: To provide a "default" implementation while allowing the user to provide their own "custom" implementation if they want.
