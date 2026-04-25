# Spring Profiles — Complete Interview Reference

## Table of Contents
1. [What are Spring Profiles?](#1-what-are-profiles)
2. [Why Use Profiles? (Environment Isolation)](#2-why-use-them)
3. [Activating Profiles (Properties, Environment, Command Line)](#3-activating)
4. [Profile-Specific Configuration (application-{profile}.properties)](#4-config-files)
5. [The Classic Interview Trap: Profile-Specific Beans (@Profile)](#5-the-classic-interview-trap-beans)
6. [Using Profiles in XML vs Java Config](#6-xml-vs-java)
7. [Default Profiles](#7-default-profiles)
8. [Combining Profiles (!, &, | logic)](#8-combining)
9. [Accessing Active Profiles via Environment API](#9-environment-api)
10. [Profiles in Integration Testing (@ActiveProfiles)](#10-testing)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What are Spring Profiles?
Spring Profiles provide a way to segregate parts of your application configuration and make it only available in certain environments (e.g., Dev, Test, Prod).

---

## 2. Why Use Profiles?
- **Database Isolation**: Use H2 in Dev and PostgreSQL in Prod.
- **Feature Toggling**: Enable Swagger UI in Dev but disable it in Prod.
- **Mocking**: Use a mock `EmailService` in Dev and a real `SmtpEmailService` in Prod.

---

## 3. Activating Profiles
There are several ways to activate a profile:
- **Environment Variable**: `SPRING_PROFILES_ACTIVE=prod`
- **JVM Argument**: `-Dspring.profiles.active=dev`
- **Application Property**: `spring.profiles.active=test`
- **Programmatically**: `SpringApplication.setAdditionalProfiles("dev")`

---

## 4. Profile-Specific Configuration
Spring Boot automatically loads properties from files following the naming convention `application-{profile}.properties`. If the `dev` profile is active, both `application.properties` and `application-dev.properties` will be loaded, with the latter taking precedence.

---

## 5. The Classic Interview Trap: @Profile on Beans
**The Trap**: You use `@Profile("dev")` on a service.
**The Problem**: If you try to autowire that service in an environment where the `dev` profile is NOT active, Spring will throw a `NoSuchBeanDefinitionException`.
**The Fix**: Always provide a "default" implementation or ensure the injection point handles the missing bean (e.g., using `Optional`).

---

## 6. XML vs Java Config
- **Java**: `@Profile("prod")` on a `@Bean` or `@Configuration` class.
- **XML**: `<beans profile="prod"> ... </beans>`

---

## 7. Default Profiles
If no profile is explicitly activated, Spring uses the `default` profile. You can change the properties for this in `application-default.properties`.

---

## 8. Combining Profiles
Spring 5.1+ supports complex profile expressions:
- `@Profile("dev & !cloud")`: Active only if `dev` is active AND `cloud` is NOT active.
- `@Profile("dev | test")`: Active if either `dev` OR `test` is active.

---

## 9. Environment API
You can inject the `Environment` object to check for active profiles programmatically:
`environment.acceptsProfiles(Profiles.of("prod"))`

---

## 10. Integration Testing
When writing tests, use `@ActiveProfiles("test")` to ensure your test context uses the correct mock beans and properties.

---

## 11. Common Mistakes
1. Hardcoding `spring.profiles.active` inside `application.properties` (defeats the purpose of environment-specific activation).
2. Forgetting that profiles are case-sensitive.
3. Over-complicating the hierarchy (keep it simple: dev, test, uat, prod).

---

## 12. Quick-Fire Interview Q&A
**Q: Can multiple profiles be active at once?**  
A: Yes, you can activate them using a comma-separated list: `dev,h2,debug`.  
**Q: Where is the best place to set the active profile in production?**  
A: As an environment variable in your deployment container (Docker/K8s) or CI/CD pipeline.
