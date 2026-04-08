# Scenario 116: Ordering Masterclass (@Order & Ordered) 📐

## Overview
How does Spring decide which bean comes first in a `List`? How do you ensure your `LogFilter` runs before the `SecurityFilter`?

Spring provides the **`@Order`** annotation and the **`Ordered`** interface to manage the precedence of components.

---

## 🚀 Key Concepts

### 1. The "Golf Score" Rule ⛳
In Spring ordering, **Lower values have Higher priority**.
- `@Order(1)` runs **BEFORE** `@Order(2)`.
- `@Order(-100)` runs **BEFORE** `@Order(0)`.
- If no order is specified, the component gets the lowest priority (`Ordered.LOWEST_PRECEDENCE`).

### 2. Ordering in Collections 📚
If you have multiple beans of the same interface, Spring can inject them all into a `List`.
- **`@Order` determines the position** in that list.
- **Example**: In our `Scenario116OrderController`, the `List<Scenario116OrderedService>` is automatically sorted.

### 3. Ordering in Chains ⛓️
For components like **Filters**, **Interceptors**, and **Aspects**, `@Order` determines the execution sequence in the chain.
- **Filter 1** (`@Order(1)`) wraps **Filter 2** (`@Order(2)`).

---

## 🏗️ Technical Details
- **Annotation**: `org.springframework.core.annotation.Order`.
- **Interface**: `org.springframework.core.Ordered` (for dynamic/runtime ordering).
- **Constant**: `Ordered.HIGHEST_PRECEDENCE` and `Ordered.LOWEST_PRECEDENCE`.

---

## 🧪 How to Test

### 1. Verify Bean Order
```bash
curl http://localhost:8080/debug-application/api/scenario116/beans
```
**Expected**: `["Alpha (Order 1)", "Beta (Order 2)"]`

### 2. Verify Filter Order
Check the response headers using `-i`:
```bash
curl -i http://localhost:8080/debug-application/api/scenario116/beans
```
**Expected Header**: `X-Scenario116-Order: First -> Second`

---

## Interview Tip 💡

**Q**: *"Does @Order control the instantiation (creation) order of beans?"*  
**A**: *"No. @Order only affects the order in which beans are **sorted** when injected into a collection (like a List) or the order of **execution** in certain Spring-managed chains (like Filters or Aspects). It does NOT guarantee that Bean A is created before Bean B at startup."*

**Q**: *"What is the difference between @Order and @Priority?"*  
**A**: *"@Order is a Spring-specific annotation. @Priority is the standard JSR-250 equivalent. Spring supports both, but @Order is more flexible as it can be used on @Bean factory methods, whereas @Priority is often limited to type-level selection."*
