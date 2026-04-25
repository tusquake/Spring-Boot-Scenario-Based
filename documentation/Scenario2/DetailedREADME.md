# Spring Bean Scopes — Complete Interview Reference

## Table of Contents
1. [What is a Bean Scope?](#1-what-is-a-bean-scope)
2. [Singleton Scope](#2-singleton-scope)
3. [Prototype Scope](#3-prototype-scope)
4. [Singleton vs Prototype — Side-by-Side](#4-singleton-vs-prototype--side-by-side)
5. [The Classic Interview Trap: Prototype inside Singleton](#5-the-classic-interview-trap-prototype-inside-singleton)
6. [Fixes for the Prototype-in-Singleton Problem](#6-fixes-for-the-prototype-in-singleton-problem)
7. [Web-Aware Scopes](#7-web-aware-scopes)
8. [Bean Lifecycle](#8-bean-lifecycle)
9. [Thread Safety](#9-thread-safety)
10. [Custom Scopes](#10-custom-scopes)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Bean Scope?

A **bean scope** controls two things:

- **How many instances** of a bean Spring creates.
- **How long** those instances live (their lifecycle).

Spring's IoC (Inversion of Control) container manages beans. When you ask the container for a bean — via `@Autowired`, `applicationContext.getBean()`, or constructor injection — the scope determines whether you get a cached instance or a brand new one.

Spring has **6 built-in scopes**:

| Scope | Available In | One instance per... |
|---|---|---|
| `singleton` | All apps | IoC container (default) |
| `prototype` | All apps | Each request to the container |
| `request` | Web apps | HTTP request |
| `session` | Web apps | HTTP session |
| `application` | Web apps | `ServletContext` lifecycle |
| `websocket` | Web apps | WebSocket session |

---

## 2. Singleton Scope

### Definition

Spring creates exactly **one** instance of the bean per IoC container. Every injection point and every `getBean()` call returns the **same object reference**.

This is the **default scope** — you do not need to declare it explicitly, but you can:

```java
@Component
// equivalent to:
@Component
@Scope("singleton")
// or:
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserService {
    // ...
}
```

### How it works internally

At startup, Spring instantiates the singleton bean and stores it in a registry (a `ConcurrentHashMap` internally). All subsequent requests hit the registry and return the cached reference — no new object is created.

```
Container startup
      │
      ▼
Spring creates UserService@abc123
      │
      ▼
Stored in singleton registry

Request 1 → getBean(UserService) → returns @abc123
Request 2 → getBean(UserService) → returns @abc123  ← same object
Request 3 → getBean(UserService) → returns @abc123  ← same object
```

### When to use it

- **Stateless services**: `@Service`, `@Repository`, `@Controller` — these hold no user-specific state.
- **Heavy resources**: Database connection pools, caches, HTTP clients — expensive to create, safe to share.
- **Configuration holders**: Beans that read config once at startup.

### What to avoid

Never store **mutable request-specific or user-specific state** in a singleton field. Since all threads share the same instance, this causes race conditions and data leakage between users.

```java
// No WRONG — shared mutable state in a singleton
@Service
public class OrderService {
    private String currentUserId; // shared across ALL threads

    public void process(String userId) {
        this.currentUserId = userId; // thread A sets this...
        // thread B overwrites it before thread A reads it back
    }
}

// Yes CORRECT — stateless; pass data as method parameters
@Service
public class OrderService {
    public void process(String userId, Order order) {
        // userId is on the stack, not a field
    }
}
```

---

## 3. Prototype Scope

### Definition

Spring creates a **new instance every time** the bean is requested — whether via injection, `getBean()`, or `ObjectFactory`.

```java
@Component
@Scope("prototype")
// or:
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();
    private String sessionId;
}
```

### How it works internally

The container does **not** cache prototype beans. Each `getBean()` call triggers full instantiation: constructor → field injection → `@PostConstruct`.

```
Request 1 → getBean(ShoppingCart) → new ShoppingCart@aaa111 (fresh)
Request 2 → getBean(ShoppingCart) → new ShoppingCart@bbb222 (fresh)
Request 3 → getBean(ShoppingCart) → new ShoppingCart@ccc333 (fresh)
```

### Lifecycle difference from singleton — critical interview point

Spring does **not** manage the full lifecycle of prototype beans:

| Lifecycle event | Singleton | Prototype |
|---|---|---|
| `@PostConstruct` | Called | Called |
| `@PreDestroy` | Yes on shutdown | No |
| Garbage collection | Spring holds reference (prevents GC) | Caller holds reference; GC when caller is done |

This means **you are responsible for cleanup** of prototype beans if they hold resources (connections, streams, etc.).

### When to use it

- **Stateful beans**: Shopping carts, wizard/form state, per-user data processors.
- **Non-thread-safe objects**: When a bean cannot be shared safely across threads.
- **Beans unique to a task**: Report generators, batch processors that accumulate state per run.

---

## 4. Singleton vs Prototype — Side-by-Side

```
Singleton                          Prototype
──────────────────────────────     ──────────────────────────────
One instance per container         New instance per request
Cached in registry                 Never cached
Long-lived (container lifetime)    Short-lived (caller controls)
@PostConstruct Yes  @PreDestroy Yes   @PostConstruct Yes  @PreDestroy No
Must be stateless (thread-safe)    Can be stateful
Default scope                      Must be declared explicitly
Low memory overhead                Higher memory (many instances)
```

### Verifying in code

```java
@RestController
public class ScopeTestController {

    @Autowired ApplicationContext ctx;

    @GetMapping("/test")
    public Map<String, Object> test() {
        SingletonBean s1 = ctx.getBean(SingletonBean.class);
        SingletonBean s2 = ctx.getBean(SingletonBean.class);

        PrototypeBean p1 = ctx.getBean(PrototypeBean.class);
        PrototypeBean p2 = ctx.getBean(PrototypeBean.class);

        return Map.of(
            "singleton_same_instance", s1 == s2,   // true
            "prototype_same_instance", p1 == p2,   // false
            "singleton_id_match",  s1.getId().equals(s2.getId()),  // true
            "prototype_id_match",  p1.getId().equals(p2.getId())   // false
        );
    }
}
```

Expected output:
```json
{
  "singleton_same_instance": true,
  "prototype_same_instance": false,
  "singleton_id_match": true,
  "prototype_id_match": false
}
```

---

## 5. The Classic Interview Trap: Prototype inside Singleton

### The Problem

If you `@Autowired` a `@Scope("prototype")` bean into a singleton, **the prototype is only created once**.

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    private final String id = UUID.randomUUID().toString();
    public String getId() { return id; }
}

@Service  // singleton by default
public class SingletonService {

    @Autowired
    private PrototypeBean prototypeBean; // No injected ONCE at singleton creation

    public String getPrototypeId() {
        return prototypeBean.getId(); // always returns the SAME id
    }
}
```

**Why?** Singletons are created once, and their dependencies are injected at that time. `prototypeBean` is captured at construction and never refreshed — so the prototype scope is effectively negated.

This is one of the most common Spring interview questions at senior level.

### Proof

```java
singletonService.getPrototypeId(); // "uuid-AAA"
singletonService.getPrototypeId(); // "uuid-AAA"  ← same! not a new prototype
singletonService.getPrototypeId(); // "uuid-AAA"  ← still same
```

---

## 6. Fixes for the Prototype-in-Singleton Problem

There are four accepted solutions. Know all of them — interviewers may ask you to compare them.

### Fix 1: ObjectFactory (recommended for Spring)

```java
@Service
public class SingletonService {

    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanFactory;

    public String getPrototypeId() {
        PrototypeBean fresh = prototypeBeanFactory.getObject(); // new instance each call
        return fresh.getId();
    }
}
```

`ObjectFactory` is a Spring interface. Each call to `getObject()` triggers prototype instantiation.

### Fix 2: Provider (recommended for portability — JSR-330)

```java
import javax.inject.Provider;

@Service
public class SingletonService {

    @Autowired
    private Provider<PrototypeBean> prototypeBeanProvider;

    public String getPrototypeId() {
        return prototypeBeanProvider.get().getId(); // new instance each call
    }
}
```

`Provider<T>` is from the JSR-330 standard (`javax.inject`). Prefer this if you want code that isn't tied to Spring's API.

### Fix 3: ApplicationContext.getBean() (works, but couples to Spring)

```java
@Service
public class SingletonService implements ApplicationContextAware {

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public String getPrototypeId() {
        return ctx.getBean(PrototypeBean.class).getId(); // new instance each call
    }
}
```

This works but violates the principle of not depending on the container directly. Avoid in production code; acceptable in tests or framework internals.

### Fix 4: Method Injection with @Lookup (Spring-only, advanced)

```java
@Service
public abstract class SingletonService {

    @Lookup
    public abstract PrototypeBean createPrototypeBean(); // Spring overrides this method

    public String getPrototypeId() {
        return createPrototypeBean().getId(); // new instance each call
    }
}
```

Spring uses CGLIB to subclass the abstract bean and override the `@Lookup` method to return a fresh prototype each time. The class must be `abstract` (or the method can be non-abstract but will be overridden).

### Comparison

| Fix | Spring-specific | Boilerplate | Best for |
|---|---|---|---|
| `ObjectFactory` | Yes | Low | Standard Spring projects |
| `Provider<T>` | No (JSR-330) | Low | Portable / multi-framework |
| `ApplicationContext` | Yes | Medium | Legacy code, quick fixes |
| `@Lookup` | Yes (CGLIB) | None at call site | Clean API, abstract services |

---

## 7. Web-Aware Scopes

These are only available in a web-aware `ApplicationContext` (e.g., `WebApplicationContext`). Using them in a non-web context throws `IllegalStateException`.

### Request Scope

One bean instance per HTTP request. Destroyed when the request completes.

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext {
    private String traceId = UUID.randomUUID().toString();
}
```

### Session Scope

One bean instance per HTTP session. Destroyed when the session expires or is invalidated.

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserPreferences {
    private String theme = "light";
    private Locale locale = Locale.ENGLISH;
}
```

### Why proxyMode is required

Request and session-scoped beans live shorter than the singleton beans that inject them. Without a proxy, Spring would inject `null` (or throw) because the real bean doesn't exist at singleton creation time.

With `proxyMode = ScopedProxyMode.TARGET_CLASS`, Spring injects a **CGLIB proxy**. Every method call on the proxy is forwarded to the real bean for the current request/session — looked up dynamically at call time.

```java
@Service // singleton
public class OrderService {

    @Autowired
    private UserPreferences userPreferences; // actually a proxy, not the real bean

    public void createOrder() {
        // at this call, the proxy looks up the real UserPreferences for THIS session
        String theme = userPreferences.getTheme();
    }
}
```

### Application Scope

One bean per `ServletContext`. Essentially singleton for a web app, but bound to the web context rather than the Spring IoC container. Rarely used in practice.

---

## 8. Bean Lifecycle

Understanding the full lifecycle is essential — interviewers frequently ask which callbacks are supported per scope.

```
Container starts
      │
      ▼
1. Bean instantiation (constructor called)
      │
      ▼
2. Dependency injection (@Autowired fields/setters populated)
      │
      ▼
3. BeanNameAware, BeanFactoryAware, ApplicationContextAware callbacks
      │
      ▼
4. BeanPostProcessor.postProcessBeforeInitialization()
      │
      ▼
5. @PostConstruct method
      │
      ▼
6. InitializingBean.afterPropertiesSet()
      │
      ▼
7. Bean is ready and in use
      │
      ▼
Container shutting down (singleton only beyond this point)
      │
      ▼
8. @PreDestroy method          ← prototype beans DO NOT reach here
      │
      ▼
9. DisposableBean.destroy()    ← prototype beans DO NOT reach here
```

### Prototype beans and cleanup

Since `@PreDestroy` is never called on prototype beans, if your prototype holds a resource you must close it manually:

```java
@Service
public class ReportService {

    @Autowired
    private ObjectFactory<ReportGenerator> generatorFactory;

    public byte[] generate(ReportRequest req) {
        ReportGenerator gen = generatorFactory.getObject();
        try {
            return gen.generate(req);
        } finally {
            gen.close(); // manual cleanup — Spring won't do it
        }
    }
}
```

---

## 9. Thread Safety

### Singleton and thread safety

All HTTP requests share the same singleton instance, which means multiple threads access it concurrently. The rule: **singleton beans must be stateless, or state must be thread-safe**.

```java
// No NOT thread-safe — shared mutable field
@Service
public class CounterService {
    private int count = 0;

    public int increment() {
        return ++count; // read-modify-write is not atomic
    }
}

// Yes Thread-safe — using AtomicInteger
@Service
public class CounterService {
    private final AtomicInteger count = new AtomicInteger(0);

    public int increment() {
        return count.incrementAndGet();
    }
}

// Yes Thread-safe — using synchronized
@Service
public class CounterService {
    private int count = 0;

    public synchronized int increment() {
        return ++count;
    }
}
```

### Prototype and thread safety

Since each caller gets their own instance, prototype beans are inherently safe from **cross-thread** contamination — as long as you don't share an instance between threads yourself.

---

## 10. Custom Scopes

You can define your own scope by implementing `org.springframework.beans.factory.config.Scope`:

```java
public class TenantScope implements Scope {

    private final Map<String, Map<String, Object>> tenantBeans = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        String tenantId = TenantContext.getCurrentTenantId();
        return tenantBeans
            .computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(name, k -> objectFactory.getObject());
    }

    @Override
    public Object remove(String name) {
        String tenantId = TenantContext.getCurrentTenantId();
        Map<String, Object> beans = tenantBeans.get(tenantId);
        return beans != null ? beans.remove(name) : null;
    }

    // ... other methods
}

// Register the scope
@Configuration
public class TenantScopeConfig {

    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("tenant", new TenantScope());
        return configurer;
    }
}

// Use it
@Component
@Scope("tenant")
public class TenantCache { ... }
```

---

## 11. Common Mistakes

### 1. Storing state in a singleton

Covered in Section 2. Keep singleton beans stateless.

### 2. Injecting prototype into singleton without ObjectFactory

Covered in Section 5. Always use `ObjectFactory`, `Provider`, or `@Lookup`.

### 3. Using web scopes without a proxy

Forgetting `proxyMode = ScopedProxyMode.TARGET_CLASS` on request/session-scoped beans injected into singletons results in a `NullPointerException` or `BeanCreationException` at runtime.

### 4. Expecting @PreDestroy on prototypes

Spring does not call `@PreDestroy` on prototype beans. Resource cleanup must be manual.

### 5. Assuming one container = one singleton

If your application has a **parent and child context** (common in Spring MVC apps), a singleton bean defined in both contexts results in **two instances** — one per context. This often surprises developers debugging why configuration changes in one context aren't visible in another.

### 6. Over-using prototype scope

Prototype is not a replacement for `new`. If your bean has no Spring dependencies and doesn't need injection, just use `new`. Prototype scope adds overhead and complexity for no benefit in that case.

---

## 12. Quick-Fire Interview Q&A

**Q: What is the default scope in Spring?**
A: `singleton`. One instance per IoC container.

**Q: What is the difference between singleton scope and the Singleton design pattern?**
A: The Singleton design pattern enforces one instance per JVM/ClassLoader. Spring's singleton scope enforces one instance per **Spring IoC container**. If you have multiple containers (e.g., parent + child), you can have multiple "singletons" of the same type.

**Q: Does Spring call @PreDestroy on prototype beans?**
A: No. Spring initializes prototype beans but does not manage their destruction. The caller is responsible for cleanup.

**Q: How do you inject a prototype bean into a singleton correctly?**
A: Use `ObjectFactory<T>`, `Provider<T>` (JSR-330), `ApplicationContext.getBean()`, or `@Lookup` method injection.

**Q: What is proxyMode and when is it needed?**
A: `proxyMode = ScopedProxyMode.TARGET_CLASS` tells Spring to inject a CGLIB proxy instead of the real bean. It is required when injecting shorter-lived beans (request, session, prototype) into longer-lived beans (singleton), so the proxy can look up the correct real instance at call time.

**Q: Can you use request scope in a non-web application?**
A: No. Request, session, and application scopes require a `WebApplicationContext`. Using them in a standalone app throws `IllegalStateException`.

**Q: What happens if you annotate a @Controller with @Scope("prototype")?**
A: It is unusual but valid. Spring MVC typically assumes controllers are singletons; using prototype on a controller can cause issues with certain MVC internals. It is almost never the right solution — prefer moving state to session or request-scoped beans.

**Q: How is prototype scope different from calling `new`?**
A: With prototype scope, Spring still manages instantiation, field injection, `@Autowired` dependencies, and `@PostConstruct`. Calling `new` bypasses all of that — you get a plain Java object with no injections.

**Q: A singleton bean has a field `List<String> items`. Multiple requests add to this list. What happens?**
A: All requests share the same list — it is a race condition. Items from different requests intermingle, and concurrent modifications throw `ConcurrentModificationException`. Fix: make the list a local variable, use a `ThreadLocal`, or make the bean prototype-scoped.

**Q: What is the lifecycle order for a Spring bean?**
A: Constructor → injection → `BeanPostProcessor` before → `@PostConstruct` → `afterPropertiesSet()` → in use → `@PreDestroy` → `destroy()`. Prototype beans stop before the destruction phase.

---

*Use this document as a revision reference. Focus especially on sections 5 and 6 — the prototype-in-singleton trap is the most frequently tested senior-level topic in Spring interviews.*