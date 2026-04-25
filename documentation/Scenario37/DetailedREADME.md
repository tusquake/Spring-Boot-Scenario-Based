# Bean Selection: @Primary vs @Qualifier — Complete Interview Reference

## Table of Contents
1. [The Dependency Ambiguity Problem](#1-ambiguity-problem)
2. [What is @Primary?](#2-what-is-primary)
3. [What is @Qualifier?](#3-what-is-qualifier)
4. [@Primary vs @Qualifier: Comparison](#4-comparison)
5. [The Classic Interview Trap: Ambiguous Dependency Exception](#5-the-classic-interview-trap-ambiguous)
6. [Using Custom Qualifier Annotations](#6-custom-qualifiers)
7. [Qualifiers with @Bean Methods](#7-bean-qualifiers)
8. [Injecting All Beans of a Type (List/Map)](#8-injecting-all)
9. [The Role of Bean Names in Injection](#9-bean-names)
10. [Dynamic Bean Selection with ApplicationContext](#10-dynamic-selection)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. The Dependency Ambiguity Problem
Ambiguity happens when you have one interface (e.g., `NotificationService`) and multiple implementations (`EmailService`, `SmsService`). If you try to autowire the interface, Spring doesn't know which one to pick and throws a `NoUniqueBeanDefinitionException`.

---

## 2. What is @Primary?
`@Primary` is used to give precedence to a particular bean when multiple beans of the same type are available. It's like saying, "Use this one by default."

---

## 3. What is @Qualifier?
`@Qualifier` is used at the injection point (constructor or field) to specify exactly which bean you want by its name. It is more specific and overrides `@Primary`.

---

## 4. Comparison Table
| Feature | @Primary | @Qualifier |
|---|---|---|
| **Defined At** | Class Level | Injection Point |
| **Priority** | Low | High |
| **Best For** | Default implementation | Specific override |

---

## 5. The Classic Interview Trap: Ambiguous Dependency
**The Trap**: You have two beans of the same type. One is marked `@Primary`. You also use `@Qualifier` at the injection point to point to the *other* bean. 
**The Question**: Which one wins?
**The Answer**: **@Qualifier wins**. Specificity always beats the default.

---

## 6. Using Custom Qualifier Annotations
Instead of using strings (which are prone to typos), you can create your own annotations:
```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Slack {}
```

---

## 7. Qualifiers with @Bean Methods
You can also use `@Qualifier` on methods inside a `@Configuration` class to specify which dependencies should be injected into that specific bean's factory method.

---

## 8. Injecting All Beans of a Type
If you need to use all implementations (e.g., send a notification to ALL channels), you can inject a `List<NotificationService>` or `Map<String, NotificationService>`. Spring will automatically collect all matching beans.

---

## 9. The Role of Bean Names
By default, the bean name is the class name in camelCase (e.g., `EmailService` -> `emailService`). If no `@Primary` or `@Qualifier` is present, Spring will try to match the variable name to a bean name as a last resort.

---

## 10. Dynamic Bean Selection
In some cases, you might want to choose a bean at runtime based on user input. You can do this by injecting the `ApplicationContext` or a `BeanFactory` and calling `getBean(name, type)`.

---

## 11. Common Mistakes
1. Typo in the `@Qualifier` string name.
2. Having multiple beans marked as `@Primary` (causes a conflict).
3. Forgetting that `@Qualifier` is required even if you think the variable name is clear.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use both @Primary and @Qualifier?**  
A: Yes, but `@Qualifier` will always take precedence at the injection point.  
**Q: What happens if no @Primary and no @Qualifier are found?**  
A: Spring throws `NoUniqueBeanDefinitionException` during startup.
