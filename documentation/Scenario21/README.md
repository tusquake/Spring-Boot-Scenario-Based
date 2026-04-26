# Scenario 21: Spring Data JPA Specifications (Dynamic Filtering)

## Overview
In real-world applications, search screens often have multiple optional filters. Writing a separate repository method for every combination (e.g., `findByNameAndDept`, `findByNameAndSalary`, etc.) leads to a combinatorial explosion of methods.

**Spring Data JPA Specifications** allow you to build complex, dynamic queries programmatically using the JPA Criteria API, without writing boilerplate boilerplate code.

---

## 🛠️ The Strategy

### 1. `JpaSpecificationExecutor`
To use specifications, your repository must extend `JpaSpecificationExecutor<T>`. This provides methods like `findAll(Specification<T> spec)`.

### 2. Building Specifications
A `Specification` is essentially a functional interface that defines a `toPredicate` method. You can chain multiple specifications using `.and()` and `.or()`.

```java
Specification<Employee> spec = Specification.where(null);
if (name != null) {
    spec = spec.and((root, query, cb) -> cb.like(root.get("name"), "%" + name + "%"));
}
```

---

## 🧪 Testing the Scenario

Try these `curl` commands to test dynamic filtering:

1. **Search by Department (IT)**:
```bash
curl "http://localhost:8080/debug-application/api/scenario21/search?dept=IT"
```

2. **Search by Name (Bob)**:
```bash
curl "http://localhost:8080/debug-application/api/scenario21/search?name=Bob"
```

3. **Combined Search (IT + Min Salary 80k)**:
```bash
curl "http://localhost:8080/debug-application/api/scenario21/search?dept=IT&minSalary=80000"
```

4. **Empty Search (Returns All)**:
```bash
curl "http://localhost:8080/debug-application/api/scenario21/search"
```

---

## Interview Tip 💡
**Q**: *"When should you use Specifications vs QueryDSL?"*
**A**: *"**Specifications** are built into Spring Data JPA and don't require code generation, making them simpler for basic dynamic queries. **QueryDSL** requires a plugin for code generation (Q-classes) but provides better type-safety and is more powerful for extremely complex joins and subqueries. Use Specifications for standard dynamic filtering."*
